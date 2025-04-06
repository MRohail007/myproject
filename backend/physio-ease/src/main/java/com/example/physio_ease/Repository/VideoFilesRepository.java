package com.example.physio_ease.Repository;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Entity.VideoFilesEntity;
import com.example.physio_ease.Enumeration.ExerciseType;
import com.example.physio_ease.dto.VideoResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoFilesRepository extends JpaRepository<VideoFilesEntity, Long> {
    Optional<VideoFilesEntity>findVideoFilesEntitiesById(Long id);
    List<VideoFilesEntity> findByAssignedPatient(PatientEntity assignedPatient);

    List<VideoFilesEntity> findByAssignedPatient_IdAndCategoryAndExerciseDay(Long patientId, ExerciseType category, String exerciseDay);

    List<VideoFilesEntity> findByAssignedPatient_Id(Long patientId);

    @Query("SELECT v FROM VideoFilesEntity v WHERE v.assignedPatient.id = :assigned_patient_id ORDER BY  v.createdAt DESC")
    List<VideoFilesEntity> findLatestVideoByAssignedPatientId(@Param("assigned_patient_id") Long patientId, Pageable pageable);

    VideoFilesEntity findByIdAndAssignedPatientId(Long videoId, Long patientId);
}
