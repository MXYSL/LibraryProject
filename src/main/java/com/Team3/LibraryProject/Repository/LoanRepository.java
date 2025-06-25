package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Loan;
import com.Team3.LibraryProject.Entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderAndStatus(Reader reader, String status);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.reader = :reader AND l.status = 'ACTIVE'")
    long countActiveLoansForReader(@Param("reader") Reader reader);
    
    @Query("SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans();
    
    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN :startDate AND :endDate")
    List<Loan> findLoansBetweenDates(@Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);
    
    @Query("SELECT l FROM Loan l WHERE l.returnDate BETWEEN :startDate AND :endDate")
    List<Loan> findReturnsBetweenDates(@Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT l FROM Loan l WHERE l.reader = :reader ORDER BY l.loanDate DESC")
    List<Loan> findByReaderOrderByLoanDateDesc(@Param("reader") Reader reader);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.reader = :reader AND l.loanDate = :date")
    long countLoansByReaderAndDate(@Param("reader") Reader reader, @Param("date") LocalDate date);
}