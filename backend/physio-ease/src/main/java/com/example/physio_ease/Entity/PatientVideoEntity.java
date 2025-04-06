package com.example.physio_ease.Entity;

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
@Table(name = "patientVideos") //Naming the table
@JsonInclude(NON_DEFAULT)
public class PatientVideoEntity extends Auditable {
    // (day, time, videoUrl, videoId, patientId, physiotherapistId, completed)
    private String fileName;
//    private String exerciseDay; // this is the day of the exercise
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "patient_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("patient_id")
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physiotherapist_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("physiotherapist_id")
    private PhysiotherapistEntity physiotherapist;
//    private String feedback; // JUST IN CASE WE DONT HAVE A FEEDBACK IMPLEMENTATION

}
