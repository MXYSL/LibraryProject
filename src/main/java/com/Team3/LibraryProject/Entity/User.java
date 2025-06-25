package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    @Id
    private String id; // A01, B0001, C01
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Column(name = "user_type", insertable = false, updatable = false)
    private String userType;
}