package com.example.physio_ease.dto;

import com.example.physio_ease.Enumeration.ExerciseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadDTO {
    /*
    ==========
    Todo: ADD STATUS FIELD FOR UPLOAD
    ==========
     */
    private String title;
    private String description;
    private String videoUrl;
    private ExerciseType category;
    private Long duration;
    private Long fileSize; // Size of video in bytes
    private String fileType; // Mp4, Mov
    @JsonProperty("physiotherapist_id")
    private Long physiotherapist_id;
    private Long assigned_patient_id;
}
