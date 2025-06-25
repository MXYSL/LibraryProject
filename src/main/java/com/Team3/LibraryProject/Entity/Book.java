package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
    private String publisher;
    
    @Column
    private String edition;
    
    @Column(name = "entry_date")
    private LocalDate entryDate;
    
    @Column(name = "total_quantity")
    private Integer totalQuantity;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Column(unique = true)
    private String isbn;
    
    @Column(columnDefinition = "TEXT")
    private String synopsis;
    
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @Column(name = "cover_image_url")
    private String coverImageUrl;
    
    @Column(name = "material_type")
    private String materialType; // "BOOK", "MAGAZINE", "DIGITAL"
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Loan> loans;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}