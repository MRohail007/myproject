package com.example.physio_ease.Repository;

import com.example.physio_ease.Entity.AchievementEntity;
import com.example.physio_ease.Entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Long> {
    AchievementEntity findByAchievementTypeAndPatientId(String achievementType, Long Id);

    List<AchievementEntity> findByPatientId(Long patientId);
}
