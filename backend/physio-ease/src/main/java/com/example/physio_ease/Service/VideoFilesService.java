package com.example.physio_ease.Service;

import com.example.physio_ease.Entity.PatientVideoEntity;
import com.example.physio_ease.Entity.VideoFilesEntity;
import com.example.physio_ease.dto.VideoCompletionUpdateRequest;
import com.example.physio_ease.dto.VideoResponseDTO;
import com.example.physio_ease.dto.VideoUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoFilesService {
    VideoFilesEntity uploadVideoExerciseToPatient(Long physiotherapistId, MultipartFile videoFile, VideoUploadDTO videoUploadDTO) throws IOException;

    List<VideoResponseDTO> getAllVideos();
    List<VideoResponseDTO> getVideoByAssignedPatient(Long patientId);
    public boolean markVideoAsCompleted(VideoCompletionUpdateRequest request, Long patientId);
    PatientVideoEntity uploadExerciseVideo(MultipartFile exerciseVideo, Long PatientId, Long PhysiotherapistId);
    List<VideoResponseDTO>getLatestVideosForPatient(Long patientId);
    //List<VideoResponseDTO> getVideo(Long Id);
}
