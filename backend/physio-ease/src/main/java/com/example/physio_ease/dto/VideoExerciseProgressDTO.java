package com.example.physio_ease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoExerciseProgressDTO {
    private String exerciseTitle;
    private String category;
    private boolean completed;
    private String exerciseDay;
    private double completedValue;
    private double playbackProgress;
//  private int completedVideo;  Todo: Add completedVideo based on video category
}
