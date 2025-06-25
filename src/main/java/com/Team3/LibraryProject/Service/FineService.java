package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FineService {
    
    @Autowired
    private FineRepository fineRepository;
    
    private static final BigDecimal LATE_FINE_PER_DAY = new BigDecimal("200");
    private static final BigDecimal DAMAGE_FINE = new BigDecimal("100");
    
    public Fine createLateFine(Loan loan) {
        if (loan.getDueDate().isBefore(LocalDate.now())) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
            BigDecimal amount = LATE_FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            
            Fine fine = new Fine();
            fine.setReader(loan.getReader());
            fine.setLoan(loan);
            fine.setFineType("LATE_RETURN");
            fine.setAmount(amount);
            fine.setFineDate(LocalDate.now());
            fine.setStatus("PENDING");
            fine.setDescription("Multa por retraso en devolución");
            fine.setDaysOverdue((int) daysOverdue);
            
            return fineRepository.save(fine);
        }
        return null;
    }
    
    public Fine createDamageFine(Loan loan) {
        Fine fine = new Fine();
        fine.setReader(loan.getReader());
        fine.setLoan(loan);
        fine.setFineType("DAMAGED_BOOK");
        fine.setAmount(DAMAGE_FINE);
        fine.setFineDate(LocalDate.now());
        fine.setStatus("PENDING");
        fine.setDescription("Multa por libro dañado");
        
        return fineRepository.save(fine);
    }
    
    public void payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
            .orElseThrow(() -> new RuntimeException("Multa no encontrada"));
        
        if ("PAID".equals(fine.getStatus())) {
            throw new RuntimeException("La multa ya está pagada");
        }
        
        fine.setStatus("PAID");
        fine.setPaymentDate(LocalDate.now());
        fineRepository.save(fine);
    }
    
    public boolean hasPendingFines(Reader reader) {
        List<Fine> pendingFines = fineRepository.findByReaderAndStatus(reader, "PENDING");
        return !pendingFines.isEmpty();
    }
    
    public BigDecimal getTotalPendingFinesForReader(Reader reader) {
        BigDecimal total = fineRepository.getTotalPendingFinesForReader(reader);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public List<Fine> getPendingFinesForReader(Reader reader) {
        return fineRepository.findByReaderAndStatus(reader, "PENDING");
    }
    
    public BigDecimal getTotalPendingFines() {
        BigDecimal total = fineRepository.getTotalPendingFines();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public long getTotalPendingFinesCount() {
        return fineRepository.countPendingFines();
    }
}