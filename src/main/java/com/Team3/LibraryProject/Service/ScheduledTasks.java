package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Repository.LoanRepository;
import com.Team3.LibraryProject.Entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduledTasks {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private FineService fineService;
    
    @Autowired
    private ReservationService reservationService;
    
    // Ejecutar cada día a las 00:01
    @Scheduled(cron = "0 1 0 * * ?")
    public void processOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans();
        
        for (Loan loan : overdueLoans) {
            if (!"OVERDUE".equals(loan.getStatus())) {
                loan.setStatus("OVERDUE");
                loanRepository.save(loan);
                
                // Crear multa por retraso
                fineService.createLateFine(loan);
            }
        }
    }
    
    // Ejecutar cada hora
    @Scheduled(fixedRate = 3600000)
    public void expireOldReservations() {
        reservationService.expireOldReservations();
    }
}