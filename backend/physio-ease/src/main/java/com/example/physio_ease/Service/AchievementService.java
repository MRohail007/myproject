package com.example.physio_ease.Service;

import com.example.physio_ease.dto.AchievementDTO;
import org.apache.tomcat.jni.LibraryNotFoundError;

import java.util.List;

public interface AchievementService {
    List<AchievementDTO> getAchievements();
    void updatePatientAchievements(Long patientId);
}
