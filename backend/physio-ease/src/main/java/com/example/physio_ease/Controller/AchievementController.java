package com.example.physio_ease.Controller;


import com.example.physio_ease.Service.impl.AchievementServiceImpl;
import com.example.physio_ease.dto.AchievementDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/video/")
public class AchievementController {

    private final AchievementServiceImpl achievementService;

    /*
    =================
    Manually triggers the evaluation of achievements for a specific patient
    =================
     */
    @GetMapping("/update/achievements/{patientId}")
    public String updateAchievements(@PathVariable Long patientId) {
        achievementService.updatePatientAchievements(patientId);
        return "Achievements updated successfully for patient ID: " + patientId;
    }

    /*
    =================
    Fetches all achievements for a specific patient, including their
    isEarned status.
    =================
     */
    @GetMapping("/achievements/patient/{patientId}")
    public ResponseEntity<List<AchievementDTO>> getAchievementsForPatient(@PathVariable Long patientId) {
        List<AchievementDTO> achievements = achievementService.getAchievementsForPatient(patientId);
        return ResponseEntity.ok(achievements); // Return the list of achievements as JSON
    }


}
