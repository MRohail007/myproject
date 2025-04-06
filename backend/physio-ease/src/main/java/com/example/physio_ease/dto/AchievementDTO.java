package com.example.physio_ease.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AchievementDTO {
    private Long id;
    private String achievementType;
    private String description;
    private boolean earned;
    private String icon;
}
