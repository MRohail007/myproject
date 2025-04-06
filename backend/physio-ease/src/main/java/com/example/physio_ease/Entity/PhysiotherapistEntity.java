package com.example.physio_ease.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "physiotherapists")
public class PhysiotherapistEntity extends UserEntity {
    private String registrationNumber; // Might not need
    private String specialty;
    private String telephoneNo;
    private String qualification; // For certification
    private String clinicName; // for smaller clinic
    private String availability;
    private Long experience;
    private String location;
    private String slot;
    private String image;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity userEntity;

    @OneToMany(mappedBy = "physiotherapist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VideoFilesEntity> videos = new ArrayList<>();

}
