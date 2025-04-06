package com.example.physio_ease.Service.impl;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Entity.VideoFilesEntity;
import com.example.physio_ease.Enumeration.ExerciseType;
import com.example.physio_ease.Exception.ApiException;
import com.example.physio_ease.Repository.VideoFilesRepository;
import com.example.physio_ease.Service.VideoProgressService;
import com.example.physio_ease.dto.ProgressTrackingResponse;
import com.example.physio_ease.dto.VideoExerciseProgressDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VideoProgressServiceImpl implements VideoProgressService {

    private final VideoFilesRepository videoFilesRepository;
//    @Override
//    public ExerciseProgressSummary getProgressForPatient(PatientEntity patientId) {
//        // Todo: Move some to utils
//        List<VideoFilesEntity> patientVideos = videoFilesRepository.findByAssignedPatient(patientId);
//
//        List<VideoExerciseProgressDTO> completedExercises = patientVideos.stream()
//                .filter(VideoFilesEntity::getIsCompleted)
//                .map(this::mapToProgressDTO) // Todo: Avoid using the map.
//                .collect(Collectors.toList());
//
//        int totalVideos = patientVideos.size();
//        int completedVideos = completedExercises.size();
//        double completedPercentage = totalVideos > 0 ? (double) completedVideos / totalVideos * 100 : 0.0;
//
//        return new ExerciseProgressSummary(
//                completedExercises,
//                totalVideos,
//                completedVideos,
//                completedPercentage
//        );
//    }

    @Override
    public VideoExerciseProgressDTO mapToProgressDTO(VideoFilesEntity video) {
        //double completedValue = calculateCompletionValue(video); // Calculation has been passed in the code
        return new VideoExerciseProgressDTO(
                video.getTitle(),
                video.getCategory().toString(),
                video.getIsCompleted(),
                video.getExerciseDay(),
                video.getCompletedValue(),
                video.getPlaybackProgress()
                //completedValue
        );
    }

    /*
    ===========
    Helper Method for calculating video completion value
    ===========
     */
    public void calculateCompletionValue(VideoFilesEntity currentVideo) {
        // Get all video in the same category and exercise day for the same patient
        List<VideoFilesEntity> similarVideo = videoFilesRepository.findByAssignedPatient_IdAndCategoryAndExerciseDay(
                currentVideo.getAssignedPatient().getId(),
                currentVideo.getCategory(),
                currentVideo.getExerciseDay()
        );
        if (similarVideo.isEmpty()) {
            System.out.println("No similar videos found for completion value calculation.");
        }

        long completedCount = similarVideo.stream()
                .filter(VideoFilesEntity::getIsCompleted)
                .count();
        double completedValue = (completedCount / (double) similarVideo.size())  * 100;
        currentVideo.setCompletedValue(completedValue);
        videoFilesRepository.save(currentVideo);

        System.out.println("Updated completion value: " + completedValue);
    }

//    @Override
//    public VideoExerciseProgressDTO markVideoAsCompleted(Long videoId) {
//        VideoFilesEntity video  = videoFilesRepository.findById(videoId)
//                .orElseThrow(() -> new ApiException("Video not found for progress data"));
//        video.setIsCompleted(true);
//        VideoFilesEntity saveVideo = videoFilesRepository.save(video);
//
//        return mapToProgressDTO(saveVideo);
//    }

    @Override
    public VideoExerciseProgressDTO getVideoProgress(Long videoId) {
        return videoFilesRepository.findVideoFilesEntitiesById(videoId)
                .map(this::mapToProgressDTO)
                .orElseThrow(()-> new ApiException("Video not found with id: " + videoId));
    }

    @Override
    public List<ProgressTrackingResponse> getPatientByCategory(Long patientId) {
        var videos = videoFilesRepository.findByAssignedPatient_Id(patientId);

        Map<ExerciseType, Double> categoryProgressMap = new HashMap<>();

        for (VideoFilesEntity video : videos) {
            ExerciseType category = video.getCategory();
            Double playbackProgress = video.getPlaybackProgress();

            if (playbackProgress == null) {
                playbackProgress = 0.0;
            }

            categoryProgressMap.put(category, categoryProgressMap.getOrDefault(category, 0.0) + playbackProgress);
        }

        // Convert the map to a list of responses
            List<ProgressTrackingResponse> progressResponse = new ArrayList<>();
            for(Map.Entry<ExerciseType, Double> entry : categoryProgressMap.entrySet()) {
                progressResponse.add(new ProgressTrackingResponse(entry.getKey(), entry.getValue()));
            }
            return progressResponse;
    }

    @Override
    public VideoExerciseProgressDTO getVideoProgress(Long videoId, Long patientId) {
        var video = videoFilesRepository.findByIdAndAssignedPatientId(videoId, patientId);
        return mapToProgressDTO(video);
    }


    public List<VideoExerciseProgressDTO> getVideoProgressByPatient(PatientEntity assignedPatient) {
        List<VideoFilesEntity> videoFiles = videoFilesRepository.findByAssignedPatient(assignedPatient);
        return videoFiles.stream()
                .map(this::mapToProgressDTO)
                .collect(Collectors.toList());
    }
}
