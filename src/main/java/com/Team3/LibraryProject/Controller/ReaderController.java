package com.Team3.LibraryProject.Controller;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Service.*;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/reader")
public class ReaderController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private FineService fineService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        Reader reader = readerRepository.findById(user.getId()).orElse(null);
        if (reader != null) {
            model.addAttribute("activeLoans", loanService.getActiveLoansForReader(reader));
            model.addAttribute("pendingFines", fineService.getPendingFinesForReader(reader));
            model.addAttribute("activeReservations", reservationService.getActiveReservationsForReader(reader));
        }
        
        return "reader/dashboard";
    }
    
    @GetMapping("/catalog")
    public String catalog(@RequestParam(required = false) String title,
                         @RequestParam(required = false) String author,
                         @RequestParam(required = false) String publisher,
                         @RequestParam(required = false) String edition,
                         @RequestParam(required = false) Long genreId,
                         HttpSession session,
                         Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        List<Book> books;
        if (title != null || author != null || publisher != null || edition != null || genreId != null) {
            books = bookService.searchBooks(title, author, publisher, edition, genreId);
        } else {
            books = bookService.getAvailableBooks();
        }
        
        model.addAttribute("books", books);
        model.addAttribute("genres", bookService.getAllGenres());
        
        return "reader/catalog";
    }
    
    @PostMapping("/reserve/{bookId}")
    public String reserveBook(@PathVariable Long bookId,
                             @RequestParam int quantity,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            reservationService.createReservation(user.getId(), bookId, quantity);
            model.addAttribute("success", "Libro apartado exitosamente. Tienes 24 horas para recogerlo.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al apartar libro: " + e.getMessage());
        }
        
        return "redirect:/reader/catalog";
    }
    
    @GetMapping("/history")
    public String loanHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        Reader reader = readerRepository.findById(user.getId()).orElse(null);
        if (reader != null) {
            model.addAttribute("loanHistory", loanService.getLoanHistory(reader));
        }
        
        return "reader/history";
    }
    
    @PostMapping("/rate/{bookId}")
    public String rateBook(@PathVariable Long bookId,
                          @RequestParam int stars,
                          @RequestParam(required = false) String comment,
                          HttpSession session,
                          Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            // Implementar servicio de calificación
            // ratingService.createRating(user.getId(), bookId, stars, comment);
            model.addAttribute("success", "Calificación agregada exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al calificar: " + e.getMessage());
        }
        
        return "redirect:/reader/history";
    }
    
    @GetMapping("/fines")
    public String viewFines(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        Reader reader = readerRepository.findById(user.getId()).orElse(null);
        if (reader != null) {
            model.addAttribute("fines", fineService.getPendingFinesForReader(reader));
            model.addAttribute("totalAmount", fineService.getTotalPendingFinesForReader(reader));
        }
        
        return "reader/fines";
    }
    
    @PostMapping("/fines/pay/{fineId}")
    public String payFine(@PathVariable Long fineId,
                         HttpSession session,
                         Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            fineService.payFine(fineId);
            model.addAttribute("success", "Multa pagada exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al pagar multa: " + e.getMessage());
        }
        
        return "redirect:/reader/fines";
    }
    
    @PostMapping("/loans/renew/{loanId}")
    public String renewLoan(@PathVariable Long loanId,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"READER".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            loanService.renewLoan(loanId);
            model.addAttribute("success", "Préstamo renovado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al renovar préstamo: " + e.getMessage());
        }
        
        return "redirect:/reader/dashboard";
    }
}