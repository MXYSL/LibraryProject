package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Reservation;
import com.Team3.LibraryProject.Entity.Book;
import com.Team3.LibraryProject.Entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReaderAndStatus(Reader reader, String status);
    
    List<Reservation> findByBookAndStatusOrderByReservationDateAsc(Book book, String status);
    
    @Query("SELECT r FROM Reservation r WHERE r.expiryDate < :currentTime AND r.status = 'ACTIVE'")
    List<Reservation> findExpiredReservations(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reader = :reader AND r.status = 'ACTIVE'")
    long countActiveReservationsForReader(@Param("reader") Reader reader);
}