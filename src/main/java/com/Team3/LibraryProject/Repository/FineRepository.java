package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Fine;
import com.Team3.LibraryProject.Entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByReaderAndStatus(Reader reader, String status);
    
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.status = 'PENDING'")
    BigDecimal getTotalPendingFines();
    
    @Query("SELECT COUNT(f) FROM Fine f WHERE f.status = 'PENDING'")
    long countPendingFines();
    
    @Query("SELECT f FROM Fine f WHERE f.fineDate BETWEEN :startDate AND :endDate")
    List<Fine> findFinesBetweenDates(@Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.reader = :reader AND f.status = 'PENDING'")
    BigDecimal getTotalPendingFinesForReader(@Param("reader") Reader reader);
}
