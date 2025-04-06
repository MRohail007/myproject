package com.example.physio_ease.Controller;


import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Entity.PatientVideoEntity;
import com.example.physio_ease.Entity.VideoFilesEntity;

import com.example.physio_ease.Exception.ApiException;
import com.example.physio_ease.Repository.PatientRepository;
import com.example.physio_ease.Repository.PhysiotherapistRepository;
import com.example.physio_ease.Repository.VideoFilesRepository;
import com.example.physio_ease.Service.impl.PatientServiceImpl;
import com.example.physio_ease.Service.impl.VideoFilesServiceImpl;
import com.example.physio_ease.Service.impl.VideoProgressServiceImpl;
import com.example.physio_ease.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/video")
public class VideoFilesController {

    private final VideoFilesServiceImpl videoFilesService;
    private final VideoFilesRepository videoFilesRepository;
    private final VideoProgressServiceImpl videoProgressService;
    private final PatientServiceImpl patientServiceImpl;

    /*
    ============
    PHYSIO ENDPOINT FOR UPLOADING VIDEO TO PATIENT - SAMAD
    ============
     */
    @PostMapping("/upload")
    public ResponseEntity<VideoFilesEntity> uploadVideo(@RequestParam Long physiotherapistId, @RequestParam("videoFile") MultipartFile videoFile, @ModelAttribute VideoUploadDTO videoUploadDTO) throws IOException {
        if (physiotherapistId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            VideoFilesEntity physioExerciseVideo = videoFilesService.uploadVideoExerciseToPatient(physiotherapistId, videoFile, videoUploadDTO);
            return ResponseEntity.ok(physioExerciseVideo);
        } catch (IOException e) {
            log.error("Error saving video file to database: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return a 500 Internal Server Error
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return a 500 Internal Server Error
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<VideoResponseDTO>> getAllVideo() {
        // Give a list of videos gotten from the videoFileServices
        List<VideoResponseDTO> videos = videoFilesService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    /*
    ============
    PHYSIO ENDPOINT FOR FETCHING A VIDEO - SAMAD
    Testing: Not being used anymore
    ============
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VideoResponseDTO>> getVideoByAssignedPatient(@PathVariable Long patientId) {
        List<VideoResponseDTO> videos = videoFilesService.getLatestVideosForPatient(patientId);
        return ResponseEntity.ok(videos);
    }

    /*
    ============
    PHYSIO ENDPOINT FOR STREAMING VIDEO - SAMAD (Untested)
    ============
     */
    @GetMapping("videos/{videoId}/play")
    public ResponseEntity<Resource> playVideo(@PathVariable Long videoId) throws MalformedURLException {
        VideoFilesEntity video = videoFilesRepository.findById(videoId).orElseThrow(() -> new ApiException("Video not found"));
        Path videoPath = Paths.get(video.getVideoUrl());
        Resource resource = new UrlResource(videoPath.toUri());

        // Detect the content type
        String contentType = "video/mp4";
        if (video.getVideoUrl().endsWith(".webm")) {
            contentType = "video/webm";
        } else if (video.getVideoUrl().endsWith(".ogg")) {
            contentType = "video/ogg";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + video.getTitle() + "\"").body(resource);
    }

    /*
    =================
    Endpoint for fetching videos assigned to patient
    =================
     */
    @GetMapping("videos/patient/{assignedPatientId}")
    public ResponseEntity<List<VideoResponseDTO>> getVideosByPatientId(@PathVariable Long assignedPatientId) {
        List<VideoResponseDTO> videos = videoFilesService.getVideoByAssignedPatient(assignedPatientId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(videos);
    }

    /*
    ================
    Endpoint for marking video as completed
    ================
     */
    @PutMapping("/completed/{patientId}")
    public ResponseEntity<?> markVideoAsCompleted(@RequestBody VideoCompletionUpdateRequest request, @PathVariable Long patientId) {
        boolean isUpdated = videoFilesService.markVideoAsCompleted(request, patientId);

        if (isUpdated) {
            VideoExerciseProgressDTO progress = videoProgressService.getVideoProgress(request.getId());
            return ResponseEntity.ok(progress); //Todo: Change from end and make sure it checks for status 200 instead Status 204
        } else {
            return ResponseEntity.notFound().build(); // Status 404
        }
    }


    /*
    Upload Video to Physiotherapist
    Todo : [x] Understand how video upload works
    Todo : [ ] Implement a notification when a video is uploaded
    Todo : [ ] Add extra validation to the video metadata
    Todo : [ ] DTO could be used in the future
     */
    @PostMapping("/upload-exercise-video")
    public ResponseEntity<PatientVideoEntity> uploadExerciseVideo(@RequestParam("file") MultipartFile file, @RequestParam("patientId") Long patientId, @RequestParam("physiotherapistId") Long physiotherapistId) {
        try {
            PatientVideoEntity uploadedVideo = videoFilesService.uploadExerciseVideo(file, patientId, physiotherapistId);
            return ResponseEntity.ok(uploadedVideo);
        } catch (ApiException e) {
            throw new ApiException("Video upload failed");
        }
    }

    /*
    =================
    Endpoints for getting the video progress
    =================
     */
    @GetMapping("/{assignedPatient}/progress")
    public ResponseEntity<List<VideoExerciseProgressDTO>> getAssignedPatientProgress(@PathVariable Long assignedPatient) {
        PatientEntity assignPatient = patientServiceImpl.getPatientId(assignedPatient);
        List<VideoExerciseProgressDTO> progress = videoProgressService.getVideoProgressByPatient(assignPatient);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/{videoId}/progress/{patientId}")
    public ResponseEntity<VideoExerciseProgressDTO> getVideoProgress(@PathVariable Long videoId, @PathVariable Long patientId){
        var videoProgress = videoProgressService.getVideoProgress(videoId, patientId);
        return ResponseEntity.ok(videoProgress);
    }

    /*
    =================
    Function for updating playback progress (Reusable)
    =================
     */
    @PostMapping("/update-playback-progress")
    public ResponseEntity<VideoExerciseProgressDTO> updatePlaybackProgress(@RequestParam Long videoId, @RequestParam double progress) {

        if (progress < 0 || progress > 100) {
            throw new ApiException("Progress must be between 0 and 100");
        }

        //Fetch the video entity for that id
        var video = videoFilesRepository.findById(videoId).orElseThrow(() -> new ApiException("Video not found"));

        //Track if an update if needed
        boolean shouldSave = false;

        // Update playback progress for video if changed
        if (video.getPlaybackProgress() != progress) {
            video.setPlaybackProgress(progress);
            shouldSave = true;
        }

        //Mark the video as complete if progress meets the threshold
        if (!video.getIsCompleted() && progress >= 70.0) {
            video.setIsCompleted(true);
            shouldSave = true;


            //Update the completion value
            videoProgressService.calculateCompletionValue(video);
        }

        // Save changes if necessary
        if (shouldSave) {
            videoFilesRepository.save(video);
        }

        System.out.println("Playback progress updated for videoId: {}, progress: {}, isCompleted: {}");

        var progressResponse = videoProgressService.mapToProgressDTO(video);

        return ResponseEntity.ok(progressResponse);
    }

    /*
    ====================
    Endpoints for fetching playback progress for visualisation
    ====================
     */
    @GetMapping("/progress-tracking/{patientId}")
    public ResponseEntity<List<ProgressTrackingResponse>> getPatientProgress(@PathVariable Long patientId) {
        List<ProgressTrackingResponse> progressData = videoProgressService.getPatientByCategory(patientId);

        if (progressData.isEmpty()) {
            throw new ApiException("No data for response entity");
        }

        return ResponseEntity.ok(progressData);
    }
}
