package com.example.physio_ease.Service.impl;

import com.example.physio_ease.Entity.*;
import com.example.physio_ease.Exception.ApiException;
import com.example.physio_ease.Repository.PatientVideoRepository;
import com.example.physio_ease.Repository.VideoFilesRepository;
import com.example.physio_ease.Service.VideoFilesService;
import com.example.physio_ease.dto.VideoCompletionUpdateRequest;
import com.example.physio_ease.dto.VideoResponseDTO;
import com.example.physio_ease.dto.VideoUploadDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.physio_ease.Constant.Constants.PATIENT_VIDEO_UPLOAD_DIR;
import static com.example.physio_ease.Constant.Constants.VIDEO_UPLOAD_DIR;
import static com.example.physio_ease.Utils.VideoFilesUtils.*;

@Service
@Transactional(rollbackOn = Exception.class) //Any Exception that occurs roll everything back.
@RequiredArgsConstructor
@Slf4j
public class VideoFilesServiceImpl implements VideoFilesService {

    private final VideoFilesRepository videoFilesRepository;
    private final PatientServiceImpl patientService;
    private final PhysiotherapistServiceImpl physiotherapistService;
    private final VideoProgressServiceImpl videoProgressService;
    private final AchievementServiceImpl achievementService;
    private final PatientVideoRepository patientVideoRepository;

    /*
    =============
    Create interface for the videoServices instead.
    =============
     */
    @Override
    public VideoFilesEntity uploadVideoExerciseToPatient(Long physiotherapistId,MultipartFile videoFile, VideoUploadDTO videoUploadDTO) throws IOException {
        String videoFilesUrl = saveVideoFile(videoFile);
        // Fetch the physiotherapist entity based on the ID from the DTO
        //PhysiotherapistEntity physiotherapist = findPhysiotherapistById(videoUploadDTO.getPhysiotherapist_id());
        PhysiotherapistEntity physiotherapist = physiotherapistService.findPhysiotherapistById(physiotherapistId);
        PatientEntity assignedPatient = patientService.findPatientById(videoUploadDTO.getAssigned_patient_id());

        String fileType = getVideoFileExtension(videoFile);
        Long fileSize = getFileSize(videoFile);
        Long duration = getVideoDuration(videoFile);

        var videoFiles = VideoFilesEntity.builder()
                .title(videoUploadDTO.getTitle())
                .description(videoUploadDTO.getDescription())
                .category(videoUploadDTO.getCategory())
                .videoUrl(videoFilesUrl)
                .fileType(fileType)
                .fileSize(fileSize)
                .duration(duration)
                .physiotherapist(physiotherapist)
                .assignedPatient(assignedPatient)
                .status("Unlocked")
                .build();

        return videoFilesRepository.save(videoFiles);
    }

    /*
    ===========
    Todo: Create a getVideoEntity method..that's better than getting only require fields
    ===========
     */
    @Override
    public List<VideoResponseDTO> getAllVideos() {
        List<VideoFilesEntity> videos = videoFilesRepository.findAll();
        return videos.stream()
                .filter(video -> video != null)
                .map(video -> {
                    VideoResponseDTO dto = new VideoResponseDTO();
                    dto.setId(video.getId());
                    dto.setTitle(video.getTitle());
                    dto.setDescription(video.getDescription());
                    dto.setCategory(video.getCategory().name());
                    dto.setVideoUrl(video.getVideoUrl());
                    dto.setStatus(video.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all videos assigned to a specific patient.
     * This method is utilized for fetching a patient's personal video library.
     *
     * @param patientId the ID of the patient whose videos are to be retrieved
     * @return a list of VideoResponseDTO containing video details
     * @throws ApiException if the patient is not found or no videos are assigned
     */
    @Override
    public List<VideoResponseDTO> getVideoByAssignedPatient(Long patientId) {
        // Fetch the PatientEntity using the PatientService
        PatientEntity assignPatient = patientService.getPatientId(patientId);
        if(assignPatient == null) {
            throw new ApiException("Assigned patient for video not found check services logic");
        }

        // Now use the fetched PatientEntity to get the videos
        List<VideoFilesEntity> patientVideos = videoFilesRepository.findByAssignedPatient(assignPatient);

        if (patientVideos.isEmpty()) {
            throw new ApiException("No video found for the specified patient ID");
        }

        // Transform VideoFilesEntity list to VideoResponseDTO list
        return patientVideos.stream()
                .filter(video -> video != null) // Ensure no null videos are processed
                .map(video -> {
                    VideoResponseDTO dto = new VideoResponseDTO();
                    dto.setId(video.getId());
                    dto.setPhysiotherapist_id(video.getPhysiotherapist().getId());
                    dto.setTitle(video.getTitle());
                    dto.setDescription(video.getDescription());
                    dto.setCategory(video.getCategory().name());
                    dto.setVideoUrl(video.getVideoUrl());
                    dto.setStatus(video.getStatus());
                    dto.setIsCompleted(video.getIsCompleted());
                    dto.setDay(video.getExerciseDay());
                    dto.getResourceUrl(); // Assuming this method call is intentional
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean markVideoAsCompleted(VideoCompletionUpdateRequest request, Long patientId) {
        Optional<VideoFilesEntity> videoOptional = videoFilesRepository.findVideoFilesEntitiesById(request.getId());
        if(videoOptional.isPresent()) {
            VideoFilesEntity video = videoOptional.get();
            video.setIsCompleted(request.getIsCompleted());
            video.setStatus("Locked");
            System.out.println(request.getIsCompleted());
            videoFilesRepository.save(video);

            achievementService.updatePatientAchievements(patientId);
            //Update the patient progress after marking isCompletedTrue
            videoProgressService.calculateCompletionValue(video);
            return true;
        }
        return false;
    }
    // Todo: Change entity for patient video upload, itneeds to be separete from the physiotherapist upload


    @Override
    @Transactional
    public PatientVideoEntity uploadExerciseVideo(MultipartFile exerciseVideo, Long patientId, Long physiotherapistId) {
        try {
            // GET PATIENT -> GET PHYSIOTHERAPIST -> GET THE VIDEO
            var patient = patientService.findPatientById(patientId);
            var physiotherapist = physiotherapistService.findPhysiotherapistById(physiotherapistId);

            File videoFileDirectory = new File(VIDEO_UPLOAD_DIR);

            if (!videoFileDirectory.exists()) {
                videoFileDirectory.mkdirs();
            }

            String fileName = exerciseVideo.getOriginalFilename();
            Path filePath = Paths.get( PATIENT_VIDEO_UPLOAD_DIR + fileName);


            Files.copy(exerciseVideo.getInputStream(), filePath);

            // Step 4: Use the Builder pattern to create the VideoFilesEntity
            var  video = PatientVideoEntity.builder()
                    .fileName(fileName) // Store the original file name for reference
                    .videoUrl(filePath.toString())
                    .patient(patient)
                    .physiotherapist(physiotherapist)
                    .build();

            return patientVideoRepository.save(video);
    } catch (IOException e) {
            log.error("Error occurred while uploading exercise video", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VideoResponseDTO> getLatestVideosForPatient(Long patientId) {

        // Now use the fetched PatientEntity to get the videos
        List<VideoFilesEntity> patientVideos = videoFilesRepository.findLatestVideoByAssignedPatientId(patientId,  PageRequest.of(0, 7));

        if (patientVideos.isEmpty()) {
            throw new ApiException("No videos found for the specified patient ID");
        }
        return patientVideos.stream()
                .map(video -> {
                    VideoResponseDTO dto = new VideoResponseDTO();
                    dto.setId(video.getId()); // Instead of "video.get" it will be "videoEntity."
                    dto.setPhysiotherapist_id(video.getPhysiotherapist() != null ? video.getPhysiotherapist().getId() : null);
                    dto.setTitle(video.getTitle());
                    dto.setDescription(video.getDescription());
                    dto.setCategory(video.getCategory() != null ? video.getCategory().name() : null);
                    dto.setVideoUrl(video.getVideoUrl());
                    dto.setStatus(video.getStatus());
                    dto.setIsCompleted(video.getIsCompleted());
                    dto.setDay(video.getExerciseDay());
                    dto.getResourceUrl();
                    return dto;
                })
                .collect(Collectors.toList());
    }

}


