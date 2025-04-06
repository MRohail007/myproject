package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        System.out.println("Sending email to: " + to + ", Subject: " + subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        // Set the "From" address with a display name
        message.setFrom("PhysioEase <physioease.team@gmail.com>");
        mailSender.send(message);
        System.out.println("Email sent successfully to: " + to);
    }
}