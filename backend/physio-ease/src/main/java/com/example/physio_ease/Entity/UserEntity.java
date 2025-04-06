package com.example.physio_ease.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED) // Use JOINED inheritance
public class UserEntity extends Auditable {

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    // private UserRole role;
//    @PrePersist
//    public void generateUUID() {
//        if (this.userId == null) {
//            this.userId = UUID.randomUUID().toString(); // Generate a new UUID as a String
//        }
//    }

}
