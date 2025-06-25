package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    @Autowired
    private FineService fineService;
    
    @Transactional
    public Loan createLoan(String readerId, Long bookId, int loanDays, String librarianId) {
        // Validar lector
        Optional<Reader> readerOpt = readerRepository.findById(readerId);
        if (readerOpt.isEmpty()) {
            throw new RuntimeException("Lector no encontrado");
        }
        Reader reader = readerOpt.get();
        
        // Validar libro
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado");
        }
        Book book = bookOpt.get();
        
        // Verificar disponibilidad del libro
        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("El libro no está disponible");
        }
        
        // Verificar límites del lector
        validateLoanLimits(reader);
        
        // Verificar que no tenga multas pendientes
        if (fineService.hasPendingFines(reader)) {
            throw new RuntimeException("El lector tiene multas pendientes");
        }
        
        // Validar días de préstamo (3-15 días)
        if (loanDays < 3 || loanDays > 15) {
            throw new RuntimeException("Los días de préstamo deben estar entre 3 y 15");
        }
        
        // Crear préstamo
        Loan loan = new Loan();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(loanDays));
        loan.setStatus("ACTIVE");
        loan.setRenewalCount(0);
        loan.setLoanDays(loanDays);
        
        // Actualizar cantidad disponible
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
        
        return loanRepository.save(loan);
    }
    
    private void validateLoanLimits(Reader reader) {
        // Máximo 3 libros por préstamo
        long activeLoansToday = loanRepository.countLoansByReaderAndDate(reader, LocalDate.now());
        if (activeLoansToday >= 3) {
            throw new RuntimeException("Máximo 3 libros por préstamo");
        }
        
        // Máximo 3 préstamos en diferentes días
        long totalActiveLoans = loanRepository.countActiveLoansForReader(reader);
        if (totalActiveLoans >= 9) { // 3 préstamos × 3 libros cada uno
            throw new RuntimeException("El lector ha alcanzado el límite de préstamos activos");
        }
    }
    
    @Transactional
    public Loan renewLoan(Long loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new RuntimeException("Préstamo no encontrado");
        }
        
        Loan loan = loanOpt.get();
        
        // Verificar que esté activo
        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new RuntimeException("Solo se pueden renovar préstamos activos");
        }
        
        // Verificar que no haya sido renovado antes
        if (loan.getRenewalCount() >= 1) {
            throw new RuntimeException("Solo se permite una renovación por préstamo");
        }
        
        // Verificar que no tenga multas
        if (fineService.hasPendingFines(loan.getReader())) {
            throw new RuntimeException("No se puede renovar con multas pendientes");
        }
        
        // Renovar por 5 días
        loan.setDueDate(loan.getDueDate().plusDays(5));
        loan.setRenewalCount(1);
        
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan returnBook(Long loanId, boolean isDamaged) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new RuntimeException("Préstamo no encontrado");
        }
        
        Loan loan = loanOpt.get();
        
        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new RuntimeException("El préstamo ya fue devuelto");
        }
        
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");
        
        // Actualizar cantidad disponible
        Book book = loan.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
        
        // Procesar multas por retraso
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            fineService.createLateFine(loan);
        }
        
        // Procesar multas por daño
        if (isDamaged) {
            fineService.createDamageFine(loan);
        }
        
        return loanRepository.save(loan);
    }
    
    public List<Loan> getActiveLoansForReader(Reader reader) {
        return loanRepository.findByReaderAndStatus(reader, "ACTIVE");
    }
    
    public List<Loan> getLoanHistory(Reader reader) {
        return loanRepository.findByReaderOrderByLoanDateDesc(reader);
    }
    
    public List<Loan> getOverdueLoans() {
        return loanRepository.findOverdueLoans();
    }
}