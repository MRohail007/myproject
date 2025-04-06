package com.example.physio_ease.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoCompletionUpdateRequest {
    private Long id;
    private Boolean isCompleted;
}
