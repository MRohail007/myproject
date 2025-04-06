package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Patient;
import com.example.demo.model.Physiotherapist;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PhysiotherapistRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// Add the Appointment-related imports
import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.OPTIONS }, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysiotherapistRepository physiotherapistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AppointmentRepository appointmentRepository; // Add this

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Existing endpoints (unchanged)

    // Get patient email by user ID (corrected from previous implementation)
    @GetMapping("/patients/email/{userId}")
    public ResponseEntity<String> getPatientEmail(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findByUserId(userId.toString());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found for user ID: " + userId);
        }
        User user = userOpt.get();
        return ResponseEntity.ok(user.getEmail());
    }

    @GetMapping("/physiotherapists")
public List<UserResponse> getAllPhysiotherapists() {
  return physiotherapistRepository.findAll().stream()
      .map(physiotherapist -> new UserResponse(
          physiotherapist.getUser().getId(),
          physiotherapist.getUser().getFirstName() + " " + physiotherapist.getUser().getLastName(),
          physiotherapist.getUser().getEmail(),
          "Physiotherapist",
          physiotherapist.getSpecialty()))
      .collect(Collectors.toList());
}

    @GetMapping("/patients")
    public List<UserResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> new UserResponse(
                        patient.getUser().getId(),
                        patient.getUser().getFirstName() + " " + patient.getUser().getLastName(),
                        patient.getUser().getEmail(),
                        "Patient",
                        null))
                .collect(Collectors.toList());
    }

    @GetMapping("/patients/{physiotherapistId}/messages")
    public List<UserResponse> getPatientsWhoMessaged(@PathVariable Long physiotherapistId) {
        Optional<Physiotherapist> physiotherapistOpt = physiotherapistRepository.findById(physiotherapistId);
        if (!physiotherapistOpt.isPresent()) {
            return List.of();
        }
        Long userId = physiotherapistOpt.get().getUser().getId();

        List<Message> messages = messageRepository.findByReceiver(userId, "Physiotherapist");
        List<Long> uniquePatientUserIds = messages.stream()
                .map(Message::getSenderId)
                .distinct()
                .collect(Collectors.toList());

        return uniquePatientUserIds.stream()
                .map(userIdOfPatient -> {
                    Optional<Patient> patientOpt = patientRepository.findByUserId(userIdOfPatient);
                    return patientOpt
                            .map(patient -> new UserResponse(
                                    patient.getUser().getId(),
                                    patient.getUser().getFirstName() + " " + patient.getUser().getLastName(),
                                    patient.getUser().getEmail(),
                                    "Patient",
                                    null))
                            .orElse(null);
                })
                .filter(userResponse -> userResponse != null)
                .collect(Collectors.toList());
    }

    @GetMapping("/chat/{senderId}/{receiverId}")
    public List<MessageResponse> getChatHistory(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestParam String senderType,
            @RequestParam String receiverType) {
        List<Message> messages = messageRepository.findBySenderAndReceiver(senderId, senderType, receiverId, receiverType);
        return messages.stream()
                .map(msg -> new MessageResponse(
                        msg.getId(),
                        msg.getSenderId(),
                        msg.getReceiverId(),
                        msg.getContent(),
                        msg.getFilePath(),
                        msg.getFileName(),
                        msg.getFileType(),
                        msg.isFile(),
                        msg.getTimestamp().toString()))
                .collect(Collectors.toList());
    }

    @PostMapping("/chat/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam("senderId") Long senderId,
            @RequestParam("senderType") String senderType,
            @RequestParam("receiverId") Long receiverId,
            @RequestParam("receiverType") String receiverType,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        if (senderType.equals("Patient")) {
            Optional<Patient> patientOpt = patientRepository.findByUserId(senderId);
            if (!patientOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid sender ID for Patient");
            }
        } else if (senderType.equals("Physiotherapist")) {
            Optional<Physiotherapist> physioOpt = physiotherapistRepository.findByUserId(senderId);
            if (!physioOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid sender ID for Physiotherapist");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid sender type");
        }

        if (receiverType.equals("Patient")) {
            Optional<Patient> patientOpt = patientRepository.findByUserId(receiverId);
            if (!patientOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid receiver ID for Patient");
            }
        } else if (receiverType.equals("Physiotherapist")) {
            Optional<Physiotherapist> physioOpt = physiotherapistRepository.findByUserId(receiverId);
            if (!physioOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid receiver ID for Physiotherapist");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid receiver type");
        }

        Message message = new Message();
        message.setSenderId(senderId);
        message.setSenderType(senderType);
        message.setReceiverId(receiverId);
        message.setReceiverType(receiverType);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setFile(false);

        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads/files/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.write(filePath, file.getBytes());

            String normalizedFilePath = filePath.toString().replace("\\", "/");
            message.setFilePath(normalizedFilePath);
            message.setFileName(file.getOriginalFilename());
            message.setFileType(file.getContentType());
            message.setFile(true);
        }

        messageRepository.save(message);

        MessageResponse response = new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.getFilePath(),
                message.getFileName(),
                message.getFileType(),
                message.isFile(),
                message.getTimestamp().toString());

        messagingTemplate.convertAndSend("/topic/messages/" + receiverId, response);
        messagingTemplate.convertAndSend("/topic/messages/" + senderId, response);

        if (senderType.equals("Patient") && receiverType.equals("Physiotherapist")) {
            Optional<Patient> patientOpt = patientRepository.findByUserId(senderId);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                UserResponse patientResponse = new UserResponse(
                        patient.getUser().getId(),
                        patient.getUser().getFirstName() + " " + patient.getUser().getLastName(),
                        patient.getUser().getEmail(),
                        "Patient",
                        null);
                List<Message> existingMessages = messageRepository
                        .findBySenderAndReceiver(senderId, "Patient", receiverId, "Physiotherapist");
                boolean isFirstMessage = existingMessages.size() == 1 && existingMessages.get(0).getId().equals(message.getId());
                if (isFirstMessage) {
                    messagingTemplate.convertAndSend("/topic/contacts/" + receiverId, patientResponse);
                }
            }
        }

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/appointments")
public ResponseEntity<String> scheduleAppointment(@RequestBody AppointmentRequest appointmentRequest) {
  System.out.println("POST /api/appointments called with data: " + appointmentRequest.getPhysiotherapistId());
  Long physioId;
  try {
    physioId = Long.parseLong(appointmentRequest.getPhysiotherapistId());
  } catch (NumberFormatException e) {
    return ResponseEntity.badRequest().body("Invalid physiotherapist ID format");
  }

  Optional<Physiotherapist> physioOpt = physiotherapistRepository.findByUserId(physioId);
  if (!physioOpt.isPresent()) {
    return ResponseEntity.badRequest().body("Physiotherapist not found for ID: " + physioId);
  }

  Optional<Patient> patientOpt = patientRepository.findByUserId(appointmentRequest.getPatientId());
  if (!patientOpt.isPresent()) {
    return ResponseEntity.badRequest().body("Patient not found for ID: " + appointmentRequest.getPatientId());
  }

  ZonedDateTime appointmentZonedDateTime;
  try {
    appointmentZonedDateTime = ZonedDateTime.parse(
        appointmentRequest.getDateTime(),
        DateTimeFormatter.ISO_DATE_TIME
    );
  } catch (DateTimeParseException e) {
    return ResponseEntity.badRequest().body("Invalid date format. Use ISO format (e.g., 2025-04-05T22:00:00.000Z)");
  }

  ZonedDateTime appointmentTimeUtc = appointmentZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
  ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of("UTC"));

  System.out.println("Appointment Time (UTC): " + appointmentTimeUtc);
  System.out.println("Current Time (UTC): " + nowUtc);

  if (appointmentTimeUtc.isBefore(nowUtc)) {
    return ResponseEntity.badRequest().body("Appointment time must be in the future (current UTC time: " + nowUtc + ")");
  }

  LocalDateTime appointmentTime = appointmentTimeUtc.toLocalDateTime();

  Appointment appointment = new Appointment();
  appointment.setPhysiotherapistId(physioId);
  appointment.setPatientId(appointmentRequest.getPatientId());
  appointment.setAppointmentDateTime(appointmentTime);
  appointment.setSubject(appointmentRequest.getSubject());
  appointment.setBody(appointmentRequest.getBody());
  appointment.setEmailSent(false);

  appointmentRepository.save(appointment);

  return ResponseEntity.ok("Appointment scheduled successfully");
}
}

// Existing DTO classes (unchanged)
class UserRequest {
    private String username;
    private String email;
    private String password;
    private String userType;
    private String specialty;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}

class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String userType;
    private String specialty;

    public UserResponse(Long id, String username, String email, String userType, String specialty) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.specialty = specialty;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getUserType() { return userType; }
    public String getSpecialty() { return specialty; }
}

class ChatRequest {
    private String senderType;
    private String receiverType;

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }
}

class MessageRequest {
    private Long senderId;
    private String senderType;
    private Long receiverId;
    private String receiverType;
    private String content;

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

class MessageResponse {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String filePath;
    private String fileName;
    private String fileType;
    private boolean isFile;
    private String timestamp;

    public MessageResponse(Long id, Long senderId, Long receiverId, String content, String filePath, String fileName,
            String fileType, boolean isFile, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.isFile = isFile;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getSenderId() { return senderId; }
    public Long getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public boolean isFile() { return isFile; }
    public String getTimestamp() { return timestamp; }
}

// New DTO class for appointment requests
class AppointmentRequest {
    private String physiotherapistId; // Change to String to match frontend
    private Long patientId;
    private String dateTime;
    private String subject;
    private String body;
  
    public String getPhysiotherapistId() { return physiotherapistId; }
    public void setPhysiotherapistId(String physiotherapistId) { this.physiotherapistId = physiotherapistId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
  }