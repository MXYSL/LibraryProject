package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class ReportService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private FineRepository fineRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    public List<Object[]> getMonthlyLoanReport(LocalDate startDate, LocalDate endDate) {
        // Ajustar fechas al inicio del lunes de la semana
        LocalDate adjustedStart = adjustToMonday(startDate);
        LocalDate adjustedEnd = adjustToMonday(endDate).plusDays(27); // 4 semanas
        
        return loanRepository.findLoansBetweenDates(adjustedStart, adjustedEnd)
                .stream()
                .collect(Collectors.groupingBy(
                    loan -> loan.getBook().getTitle(),
                    Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                .collect(Collectors.toList());
    }
    
    public List<Object[]> getMonthlyReturnReport(LocalDate startDate, LocalDate endDate) {
        LocalDate adjustedStart = adjustToMonday(startDate);
        LocalDate adjustedEnd = adjustToMonday(endDate).plusDays(27);
        
        return loanRepository.findReturnsBetweenDates(adjustedStart, adjustedEnd)
                .stream()
                .collect(Collectors.groupingBy(
                    loan -> loan.getBook().getTitle(),
                    Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                .collect(Collectors.toList());
    }
    
    public List<Object[]> getBooksQuantityReport() {
        return bookRepository.findAll()
                .stream()
                .map(book -> new Object[]{
                    book.getTitle(),
                    book.getTotalQuantity(),
                    book.getAvailableQuantity(),
                    book.getGenre().getName()
                })
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getFinesReport() {
        BigDecimal totalPendingFines = fineRepository.getTotalPendingFines();
        long totalPendingCount = fineRepository.countPendingFines();
        List<String> usersWithFines = readerRepository.findReadersWithPendingFines()
                .stream()
                .map(reader -> reader.getId() + " - " + reader.getName())
                .collect(Collectors.toList());
        
        return Map.of(
            "totalAmount", totalPendingFines != null ? totalPendingFines : BigDecimal.ZERO,
            "totalCount", totalPendingCount,
            "usersWithFines", usersWithFines
        );
    }
    
    public List<Object[]> getBooksWithRatings() {
        return bookRepository.findAll()
                .stream()
                .map(book -> {
                    Double avgRating = ratingRepository.getAverageRatingForBook(book);
                    return new Object[]{
                        book.getTitle(),
                        book.getAuthor(),
                        avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : "Sin calificar",
                        book.getGenre().getName()
                    };
                })
                .collect(Collectors.toList());
    }
    
    private LocalDate adjustToMonday(LocalDate date) {
        while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            date = date.minusDays(1);
        }
        return date;
    }
    
    @Autowired
    private RatingRepository ratingRepository;
}
