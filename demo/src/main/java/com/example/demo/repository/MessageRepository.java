package com.example.demo.repository;

import com.example.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.senderId = :senderId AND m.senderType = :senderType AND m.receiverId = :receiverId AND m.receiverType = :receiverType) OR (m.senderId = :receiverId AND m.senderType = :receiverType AND m.receiverId = :senderId AND m.receiverType = :senderType)")
    List<Message> findBySenderAndReceiver(Long senderId, String senderType, Long receiverId, String receiverType);

    @Query("SELECT m FROM Message m WHERE m.receiverId = :receiverId AND m.receiverType = :receiverType")
    List<Message> findByReceiver(Long receiverId, String receiverType);
    
}