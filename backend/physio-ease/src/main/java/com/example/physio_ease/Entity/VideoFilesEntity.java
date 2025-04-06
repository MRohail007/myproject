package com.example.physio_ease.Entity;

import com.example.physio_ease.Enumeration.ExerciseType;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos") //Naming the table
@JsonInclude(NON_DEFAULT)

public class VideoFilesEntity extends Auditable {

    private String title;
    private String description;
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private ExerciseType category;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "assigned_patient_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("assigned_patient_id")
    private PatientEntity assignedPatient;


    private String status;
    private String exerciseDay;
    private Boolean isCompleted;
    private Double completedValue;
    private Double playbackProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physiotherapist_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("physiotherapist_id")
    private PhysiotherapistEntity physiotherapist;

    private String thumbnailUrl;
    private Long duration;
    private Long fileSize;
    private String fileType;

}
