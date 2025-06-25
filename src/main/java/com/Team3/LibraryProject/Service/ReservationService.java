package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    @Transactional
    public Reservation createReservation(String readerId, Long bookId, int quantity) {
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
        
        // Verificar que el lector no tenga multas pendientes
        long activeReservations = reservationRepository.countActiveReservationsForReader(reader);
        if (activeReservations >= 10) { // Límite arbitrario de reservaciones activas
            throw new RuntimeException("Demasiadas reservaciones activas");
        }
        
        Reservation reservation = new Reservation();
        reservation.setReader(reader);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24 horas para recoger
        reservation.setStatus("ACTIVE");
        reservation.setQuantity(Math.min(quantity, 3)); // Máximo 3 libros por reservación
        
        return reservationRepository.save(reservation);
    }
    
    @Transactional
    public void fulfillReservation(Long reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservación no encontrada");
        }
        
        Reservation reservation = reservationOpt.get();
        
        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new RuntimeException("La reservación no está activa");
        }
        
        if (LocalDateTime.now().isAfter(reservation.getExpiryDate())) {
            reservation.setStatus("EXPIRED");
            reservationRepository.save(reservation);
            throw new RuntimeException("La reservación ha expirado");
        }
        
        reservation.setStatus("FULFILLED");
        reservationRepository.save(reservation);
    }
    
    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservación no encontrada");
        }
        
        Reservation reservation = reservationOpt.get();
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }
    
    public void expireOldReservations() {
        List<Reservation> expiredReservations = reservationRepository
            .findExpiredReservations(LocalDateTime.now());
        
        for (Reservation reservation : expiredReservations) {
            reservation.setStatus("EXPIRED");
            reservationRepository.save(reservation);
        }
    }
    
    public List<Reservation> getActiveReservationsForReader(Reader reader) {
        return reservationRepository.findByReaderAndStatus(reader, "ACTIVE");
    }
    
    public List<Reservation> getQueueForBook(Book book) {
        return reservationRepository.findByBookAndStatusOrderByReservationDateAsc(book, "ACTIVE");
    }
}