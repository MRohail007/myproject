package com.example.physio_ease.Service;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.dto.Patient;

public interface PatientService {
    PatientEntity getPatientId(Long patientId);
    Patient getPatientById(Long patientId);
    PatientEntity updatePatientProfile(Long Id, PatientEntity updatedPatient);
    Patient updatePatient(Long id, String firstName, String lastName, String email);
}
