package com.example.physio_ease.Service.impl;

import com.example.physio_ease.Entity.AchievementEntity;
import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Entity.VideoFilesEntity;
import com.example.physio_ease.Repository.AchievementRepository;
import com.example.physio_ease.Repository.PatientRepository;
import com.example.physio_ease.Repository.VideoFilesRepository;
import com.example.physio_ease.Service.AchievementService;
import com.example.physio_ease.dto.AchievementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final VideoFilesRepository videoFilesRepository;
    private final PatientRepository patientRepository;


    /**
     * Retrieves all achievements from the database and maps them to DTOs.
     *
     * @return a list of AchievementDTO representing all achievements.
     */
    @Override
    public List<AchievementDTO> getAchievements() {

        // Fetch all the achievements from the database
        List<AchievementEntity> achievements = achievementRepository.findAll();
        //Map to DTO
        return achievements.stream()
                .map(entity -> new AchievementDTO(
                        entity.getId(),
                        entity.getAchievementType(),
                        entity.getDescription(),
                        entity.isEarned(),
                        entity.getIcon()))
                .collect(Collectors.toList());
    }


    /**
     * Retrieves all achievements for a specific patient, initializing default achievements if none exist.
     *
     * @param patientId the ID of the patient whose achievements are to be retrieved
     * @return a list of AchievementDTO representing the patient's achievements
     */
    public List<AchievementDTO> getAchievementsForPatient(Long patientId) {
        // Fetch achievements for the patient from the database
        List<AchievementEntity> achievements = achievementRepository.findByPatientId(patientId);

        // If no achievements exist for the patient, initialize default achievements
        if (achievements.isEmpty()) {
            achievements = initializeDefaultAchievementsForPatient(patientId);
        }

        // Map to DTO
        return achievements.stream()
                .map(entity -> new AchievementDTO(
                        entity.getId(),
                        entity.getAchievementType(),
                        entity.getDescription(),
                        entity.isEarned(),
                        entity.getIcon()))
                .collect(Collectors.toList());
    }


    /**
     * Initializes default achievements for a patient if none exist.
     *
     * @param patientId the ID of the patient whose achievements are to be initialized
     * @return a list of AchievementEntity representing the default achievements
     */
    private List<AchievementEntity> initializeDefaultAchievementsForPatient(Long patientId) {
        List<AchievementEntity> defaultAchievements = new ArrayList<>();

        // Define default achievements
        defaultAchievements.add(createAchievementEntity("FIRST_EXERCISE", "Completed your first exercise video", " ", patientId));
        defaultAchievements.add(createAchievementEntity("WEEKLY_WARRIORS", "Completed exercises 3 times in a week", " ", patientId));
        defaultAchievements.add(createAchievementEntity("CONSISTENCY_KING", "Exercised for 30 days straight", " ", patientId));
        defaultAchievements.add(createAchievementEntity("PROGRESS_MASTER", "Uploaded 5 progress photos", " ", patientId));

        // Save default achievements to the database
        return achievementRepository.saveAll(defaultAchievements);
    }


    // Helper method to create an AchievementEntity
    private AchievementEntity createAchievementEntity(String achievementType, String description, String icon, Long patientId) {
        AchievementEntity achievement = new AchievementEntity();
        PatientEntity patientEntity = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        achievement.setAchievementType(achievementType);
        achievement.setDescription(description);
        achievement.setEarned(false);
        achievement.setIcon(icon);
        achievement.setPatient(patientEntity); // Associate the achievement with the patient
        return achievement;
    }

    /*
    =====================
    Function to update patient achievements based on video exercise
    =====================
     */
    @Override
    public void updatePatientAchievements(Long patientId) {
        List<VideoFilesEntity> videoExercise = videoFilesRepository.findByAssignedPatient_Id(patientId);

        // Validate the criteria for each achievement type
        checkAndUpdateAchievement("FIRST_EXERCISE", videoExercise.stream().anyMatch(VideoFilesEntity::getIsCompleted), patientId);
        checkAndUpdateAchievement("WEEKLY_WARRIORS", isWeeklyWarriorAchieved(videoExercise), patientId); //Todo: Write method
        // checkAndUpdateAchievement("CONSISTENCY_KING", isConsistencyKingAchieved(videoExercise)); //Todo: Write method
        //checkAndUpdateAchievement("PROGRESS_MASTER", isProgressMasteredAchieved(patientId)); //Todo: Write method
    }

    /**
     * Helper method to check and update an achievement if the criteria is met.
     * The method first checks if the achievement exists for the given patient
     * and if the criteria is met. If both conditions are true, it sets the
     * isEarned flag to true and saves the achievement to the database.
     *
     * @param achievementType the type of achievement to check
     * @param isCriteriaMet   whether the criteria for the achievement is met
     * @param patientId       the ID of the patient
     */
    private void checkAndUpdateAchievement(String achievementType, boolean isCriteriaMet, Long patientId) {
        // Retrieve the achievement from the database
        AchievementEntity achievement = achievementRepository.findByAchievementTypeAndPatientId(achievementType, patientId);
        if (achievement != null && !achievement.isEarned() && isCriteriaMet) {
            // Update the achievement if criteria is met and it is not already earned
            achievement.setEarned(true);
            achievementRepository.save(achievement);
        }
    }

    /*
    ================
    Helper method: Checking if 3 videos has been completed in the last
    3 weeks.
    ================
     */
    private boolean isWeeklyWarriorAchieved(List<VideoFilesEntity> videoExercise) {
        return videoExercise.stream()
                .filter(VideoFilesEntity::getIsCompleted)
                //Group the completed videos by their `exerciseDay` (e.g., "Monday", "Tuesday")
                .collect(Collectors.groupingBy(VideoFilesEntity::getExerciseDay))
                //Check if any group (i.e., any day) contains 3 or more completed videos
                .values().stream()
                .anyMatch(videosInDay -> videosInDay.size() >= 3);
    }

}
