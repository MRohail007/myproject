package com.example.physio_ease.dto;

import lombok.Data;

@Data
public class Patient {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String medicalHistory;
    private String dateOfBirth;

}
