package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "physiotherapists")
public class Physiotherapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "clinic_name")
    private String clinicName;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "telephone_no")
    private String telephoneNo;

    @Column(name = "availability")
    private String availability;

    @Column(name = "experience")
    private Long experience;

    @Column(name = "location")
    private String location;

    @Column(name = "booking_slot")
    private Integer bookingSlot;

    @Column(name = "image")
    private String image;

    @Column(name = "slot")
    private String slot;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getTelephoneNo() { return telephoneNo; }
    public void setTelephoneNo(String telephoneNo) { this.telephoneNo = telephoneNo; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getBookingSlot() { return bookingSlot; }
    public void setBookingSlot(Integer bookingSlot) { this.bookingSlot = bookingSlot; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
}