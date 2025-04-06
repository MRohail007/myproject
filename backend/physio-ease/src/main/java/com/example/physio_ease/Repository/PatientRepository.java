package com.example.physio_ease.Repository;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Entity.PhysiotherapistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
}
