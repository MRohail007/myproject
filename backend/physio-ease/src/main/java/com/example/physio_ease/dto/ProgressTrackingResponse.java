package com.example.physio_ease.dto;


import com.example.physio_ease.Enumeration.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressTrackingResponse {
    private ExerciseType category;
    private Double totalPlaybackProgress;
}
