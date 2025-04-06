package com.example.physio_ease.Service;

import com.example.physio_ease.Entity.VideoFilesEntity;
import com.example.physio_ease.dto.ProgressTrackingResponse;
import com.example.physio_ease.dto.VideoExerciseProgressDTO;

import java.util.List;

public interface VideoProgressService {
//    ExerciseProgressSummary getProgressForPatient(PatientEntity patientId);
    VideoExerciseProgressDTO mapToProgressDTO(VideoFilesEntity video);

    VideoExerciseProgressDTO getVideoProgress(Long videoId);

    List<ProgressTrackingResponse> getPatientByCategory(Long patientId);

    VideoExerciseProgressDTO getVideoProgress(Long videoId, Long patientId);
}
