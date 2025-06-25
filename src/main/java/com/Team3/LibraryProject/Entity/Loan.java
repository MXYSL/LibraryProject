package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    @Column(name = "loan_date")
    private LocalDate loanDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @Column
    private String status; // "ACTIVE", "RETURNED", "OVERDUE"
    
    @Column(name = "renewal_count")
    private Integer renewalCount;
    
    @Column(name = "loan_days")
    private Integer loanDays; // 3-15 días
    
    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian;
}