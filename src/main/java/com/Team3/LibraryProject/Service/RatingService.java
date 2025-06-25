package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    public Rating createRating(String readerId, Long bookId, int stars, String comment) {
        Optional<Reader> readerOpt = readerRepository.findById(readerId);
        if (readerOpt.isEmpty()) {
            throw new RuntimeException("Lector no encontrado");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        
        Reader reader = readerOpt.get();
        Book book = bookOpt.get();
        
        // Verificar si ya existe una calificación del lector para este libro
        Optional<Rating> existingRating = ratingRepository.findByReaderAndBook(reader, book);
        if (existingRating.isPresent()) {
            // Actualizar calificación existente
            Rating rating = existingRating.get();
            rating.setStars(stars);
            rating.setComment(comment);
            rating.setRatingDate(LocalDate.now());
            return ratingRepository.save(rating);
        } else {
            // Crear nueva calificación
            Rating rating = new Rating();
            rating.setReader(reader);
            rating.setBook(book);
            rating.setStars(stars);
            rating.setComment(comment);
            rating.setRatingDate(LocalDate.now());
            return ratingRepository.save(rating);
        }
    }
    
    public Double getAverageRatingForBook(Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            return null;
        }
        
        Double average = ratingRepository.getAverageRatingForBook(bookOpt.get());
        return average != null ? Math.round(average * 10.0) / 10.0 : null;
    }
    
    public List<Rating> getRatingsForBook(Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        
        return ratingRepository.findByBook(bookOpt.get());
    }
    
    public List<Rating> getRatingsByReader(String readerId) {
        Optional<Reader> readerOpt = readerRepository.findById(readerId);
        if (readerOpt.isEmpty()) {
            throw new RuntimeException("Lector no encontrado");
        }
        
        return ratingRepository.findByReader(readerOpt.get());
    }
}
