package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate; // 24 horas después
    
    @Column
    private String status; // "ACTIVE", "EXPIRED", "FULFILLED", "CANCELLED"
    
    @Column
    private Integer quantity;
}