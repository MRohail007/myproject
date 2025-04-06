package com.example.physio_ease.Utils;

import com.example.physio_ease.Entity.PatientEntity;
import com.example.physio_ease.dto.Patient;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;

public class PatientUtil {
    public static Patient fromPatientEntity(PatientEntity patientEntity) {
        Patient patient = new Patient();
        BeanUtils.copyProperties(patientEntity, patient);
        System.out.println(patientEntity.getId());
        patient.setId(patientEntity.getId());
        patient.setFirstName(patientEntity.getFirstName());
        patient.setLastName(patientEntity.getLastName());
        patient.setEmail(patientEntity.getEmail());
        patient.setMedicalHistory(patientEntity.getMedicalHistory());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        return patient;
    }

    public static  Patient toPatientEntity(PatientEntity patientEntity) {
     Patient patients = new Patient();
     BeanUtils.copyProperties(patientEntity, patients);
     patients.setId(patientEntity.getId());

     return patients;
    }
}
