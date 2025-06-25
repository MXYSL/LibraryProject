package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;
    
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    
    @Column(name = "fine_type")
    private String fineType; // "LATE_RETURN", "DAMAGED_BOOK"
    
    @Column
    private BigDecimal amount;
    
    @Column(name = "fine_date")
    private LocalDate fineDate;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @Column
    private String status; // "PENDING", "PAID"
    
    @Column
    private String description;
    
    @Column(name = "days_overdue")
    private Integer daysOverdue;
}
