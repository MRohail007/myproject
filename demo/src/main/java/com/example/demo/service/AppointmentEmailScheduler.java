package com.example.demo.service;

import com.example.demo.model.Appointment;
import com.example.demo.model.Physiotherapist;
import com.example.demo.model.User;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PhysiotherapistRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class AppointmentEmailScheduler {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PhysiotherapistRepository physiotherapistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void sendAppointmentEmails() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Running AppointmentEmailScheduler at: " + now);
        List<Appointment> appointments = appointmentRepository.findByEmailSentFalseAndAppointmentDateTimeBefore(now);
        System.out.println("Found " + appointments.size() + " appointments to process");

        for (Appointment appointment : appointments) {
            try {
                System.out.println("Processing appointment ID: " + appointment.getId() + ", Appointment Time: " + appointment.getAppointmentDateTime());
                // Get physiotherapist email
                Optional<Physiotherapist> physioOpt = physiotherapistRepository.findByUserId(appointment.getPhysiotherapistId());
                if (!physioOpt.isPresent()) {
                    System.err.println("Physiotherapist not found for ID: " + appointment.getPhysiotherapistId());
                    continue;
                }

                String physioEmail = physioOpt.get().getUser().getEmail();
                if (physioEmail == null || physioEmail.isEmpty()) {
                    System.err.println("Physiotherapist email is null or empty for ID: " + appointment.getPhysiotherapistId());
                    continue;
                }

                // Get patient username
                Optional<User> patientUserOpt = userRepository.findById(appointment.getPatientId());
                String patientUsername;
                if (patientUserOpt.isPresent()) {
                    User patientUser = patientUserOpt.get();
                    String firstName = patientUser.getFirstName() != null ? patientUser.getFirstName() : "";
                    String lastName = patientUser.getLastName() != null ? patientUser.getLastName() : "";
                    patientUsername = (firstName + " " + lastName).trim();
                    if (patientUsername.isEmpty()) {
                        patientUsername = patientUser.getEmail() != null ? patientUser.getEmail() : "Patient";
                    }
                } else {
                    System.err.println("Patient user not found for ID: " + appointment.getPatientId());
                    patientUsername = "Patient";
                }

                // Format the email body
                String emailBody = "Hello " + physioOpt.get().getUser().getFirstName() + ",\n\n" +
                                  "*** Reminder  ***\n\n" +
                                  "You have an appointment with " + patientUsername + ".\n" + "Message: " + appointment.getBody() + "\n\n" + "If you have any questions, feel free to reach out to us.\n\n" +
                                  "Warm regards,\n" +
                                  "The PhysioEase Team";

                // Send the email
                emailService.sendEmail(physioEmail, appointment.getSubject(), emailBody);
                appointment.setEmailSent(true);
                appointmentRepository.save(appointment);
                System.out.println("Email sent for appointment ID: " + appointment.getId() + " to: " + physioEmail);

            } catch (Exception e) {
                System.err.println("Failed to send email for appointment ID " + appointment.getId() + ": " + e.getMessage());
            }
        }
    }
}