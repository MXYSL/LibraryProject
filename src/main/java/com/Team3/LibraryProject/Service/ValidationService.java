package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.Reader;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class ValidationService {
    
    @Autowired
    private FineRepository fineRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    private static final Pattern RFC_PATTERN = Pattern.compile("^[A-Z]{4}[0-9]{6}[A-Z0-9]{3}$");
    private static final Pattern CURP_PATTERN = Pattern.compile("^[A-Z]{4}[0-9]{6}[MH][A-Z]{5}[0-9]{2}$");
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    
    public boolean validateRFC(String rfc) {
        return rfc != null && RFC_PATTERN.matcher(rfc.toUpperCase()).matches();
    }
    
    public boolean validateCURP(String curp) {
        return curp != null && CURP_PATTERN.matcher(curp.toUpperCase()).matches();
    }
    
    public boolean validateISBN(String isbn) {
        if (isbn == null) return true; // ISBN es opcional
        return ISBN_PATTERN.matcher(isbn.replaceAll("[- ]", "")).matches();
    }
    
    public boolean canCreateLoan(Reader reader) {
        // Verificar multas pendientes
        boolean hasPendingFines = !fineRepository.findByReaderAndStatus(reader, "PENDING").isEmpty();
        if (hasPendingFines) {
            return false;
        }
        
        // Verificar límites de préstamos
        long activeLoans = loanRepository.countActiveLoansForReader(reader);
        return activeLoans < 9; // Máximo 9 libros (3 préstamos × 3 libros cada uno)
    }
    
    public boolean canRenewLoan(Reader reader) {
        // No se puede renovar con multas pendientes
        return fineRepository.findByReaderAndStatus(reader, "PENDING").isEmpty();
    }
}