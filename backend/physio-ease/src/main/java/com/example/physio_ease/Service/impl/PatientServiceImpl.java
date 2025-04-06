package com.example.physio_ease.Service.impl;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.Exception.ApiException;
import com.example.physio_ease.Repository.PatientRepository;
import com.example.physio_ease.Service.PatientService;
import com.example.physio_ease.dto.Patient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.physio_ease.Utils.PatientUtil.fromPatientEntity;

@Service
@Transactional(rollbackOn = Exception.class) //Any Exception that occurs roll everything back.
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientEntity findPatientById(Long assigned_patient_id) {
        return patientRepository.findById(assigned_patient_id).orElseThrow(() -> new IllegalArgumentException("Unable to find assign patient id"));
    }


    @Override
    public PatientEntity getPatientId(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + patientId));
    }

    @Override
    public Patient getPatientById(Long patientId) {
        var patientEntity = patientRepository.findById(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        return fromPatientEntity(patientEntity);
    }

    @Override
    public PatientEntity updatePatientProfile(Long id, PatientEntity updatedPatient) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(updatedPatient.getFirstName());
                    patient.setLastName(updatedPatient.getLastName());
                    patient.setMedicalHistory(updatedPatient.getMedicalHistory());
                    patient.setAllergies(updatedPatient.getAllergies());
                    patient.setCurrentMedication(updatedPatient.getCurrentMedication());
                    
                    return patientRepository.save(patient);
                })
                .orElseThrow(() -> new ApiException("Patient not found with id " + id));
    }

    @Override
    public Patient updatePatient(Long id, String firstName, String lastName, String email) {
        var patientEntity = getPatientId(id);
        patientEntity.setFirstName(firstName);
        patientEntity.setLastName(lastName);
        patientEntity.setEmail(email);
        patientRepository.save(patientEntity);

        return  fromPatientEntity(patientEntity);
    }


}


