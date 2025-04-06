package com.example.physio_ease.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class PatientEntity extends UserEntity {
    private String dateOfBirth;
    private String medicalHistory; // Other things need to added(research needed)
    private String allergies;
    private String currentMedication;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @JsonProperty("user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physiotherapist_id")
    private PhysiotherapistEntity physiotherapist; // The physiotherapist associated with the patient

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AchievementEntity> achievements;
}
