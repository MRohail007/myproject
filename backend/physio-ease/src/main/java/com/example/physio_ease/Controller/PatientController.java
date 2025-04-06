package com.example.physio_ease.Controller;

import com.example.physio_ease.Exception.ApiException;
import com.example.physio_ease.Service.impl.PatientServiceImpl;
import com.example.physio_ease.dto.Patient;
import com.example.physio_ease.dto.PatientRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientServiceImpl patientService;
    /*
    ==============
    Get patient by their id
    Todo: Pass it the patient id as current logged in
          user in the frontend.
    ==============
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long patientId){
        try {
            var patientEntity = patientService.getPatientById(patientId);
            return new ResponseEntity<>(patientEntity, HttpStatus.OK);
        }catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateProfile/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id, // Capture the patient ID from the URL
            @RequestBody PatientRequest updateRequest) { // Capture the request body
        // Call the service method to update the patient
        Patient updatedPatient = patientService.updatePatient(id, updateRequest.getFirstName(), updateRequest.getLastName(), updateRequest.getEmail());
        // Return the updated patient with a 200 OK status
        return ResponseEntity.ok(updatedPatient);
    }
}
