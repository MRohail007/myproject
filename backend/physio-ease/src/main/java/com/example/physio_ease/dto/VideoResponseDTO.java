package com.example.physio_ease.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String videoUrl;
    private String status;
    private Long physiotherapist_id;
    private String day;
    private Boolean isCompleted;
    public String getResourceUrl() {
        return "/api/video/videos/" + id + "/play"; //Todo: Remove /api/video
    }
}
