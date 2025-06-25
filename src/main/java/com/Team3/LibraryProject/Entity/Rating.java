package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    @Column
    private Integer stars; // 1-5 estrellas
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "rating_date")
    private LocalDate ratingDate;
}