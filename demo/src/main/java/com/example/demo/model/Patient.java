package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "physiotherapist_id")
    private Physiotherapist physiotherapist;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "current_medication")
    private String currentMedication;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Physiotherapist getPhysiotherapist() { return physiotherapist; }
    public void setPhysiotherapist(Physiotherapist physiotherapist) { this.physiotherapist = physiotherapist; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getCurrentMedication() { return currentMedication; }
    public void setCurrentMedication(String currentMedication) { this.currentMedication = currentMedication; }
}