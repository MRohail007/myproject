package com.example.physio_ease.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhysiotherapistDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private String speciality; // Todo: Change to specialization
    private String telephoneNo;
    private String qualification;
    private String clinicName;
    private String availability;
    private Long experience;
    private String location;
    private String slot;
    private String image;

//    private UserEntity userEntity;
}
