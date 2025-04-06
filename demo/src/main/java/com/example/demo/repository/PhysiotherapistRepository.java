package com.example.demo.repository;

import com.example.demo.model.Physiotherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysiotherapistRepository extends JpaRepository<Physiotherapist, Long> {
    Optional<Physiotherapist> findByUserId(Long userId);
    Optional<Physiotherapist> findByUser_UserId(String userId);
    Optional<Physiotherapist> findByUser_Email(String email);
}