package com.Team3.LibraryProject.Controller;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Service.*;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private FineService fineService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        model.addAttribute("overdueLoans", loanService.getOverdueLoans());
        model.addAttribute("totalPendingFines", fineService.getTotalPendingFines());
        
        return "librarian/dashboard";
    }
    
    @GetMapping("/loans")
    public String manageLoans(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        model.addAttribute("readers", readerRepository.findAll());
        model.addAttribute("books", bookService.getAvailableBooks());
        
        return "librarian/loans";
    }
    
    @PostMapping("/loans/create")
    public String createLoan(@RequestParam String readerId,
                            @RequestParam Long bookId,
                            @RequestParam int loanDays,
                            HttpSession session,
                            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            loanService.createLoan(readerId, bookId, loanDays, user.getId());
            model.addAttribute("success", "Préstamo creado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear préstamo: " + e.getMessage());
        }
        
        model.addAttribute("readers", readerRepository.findAll());
        model.addAttribute("books", bookService.getAvailableBooks());
        
        return "librarian/loans";
    }
    
    @PostMapping("/loans/return/{id}")
    public String returnBook(@PathVariable Long id,
                            @RequestParam(defaultValue = "false") boolean isDamaged,
                            HttpSession session,
                            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            loanService.returnBook(id, isDamaged);
            model.addAttribute("success", "Libro devuelto exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al devolver libro: " + e.getMessage());
        }
        
        return "redirect:/librarian/loans";
    }
    
    @PostMapping("/loans/renew/{id}")
    public String renewLoan(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            loanService.renewLoan(id);
            model.addAttribute("success", "Préstamo renovado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al renovar préstamo: " + e.getMessage());
        }
        
        return "redirect:/librarian/loans";
    }
    
    @GetMapping("/books/update-quantity")
    public String updateBookQuantity(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        model.addAttribute("books", bookRepository.findAll());
        
        return "librarian/update-quantity";
    }
    
    @PostMapping("/books/update-quantity/{id}")
    public String updateQuantity(@PathVariable Long id,
                                @RequestParam int newQuantity,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            bookService.updateBookQuantity(id, newQuantity);
            model.addAttribute("success", "Cantidad actualizada exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar cantidad: " + e.getMessage());
        }
        
        model.addAttribute("books", bookRepository.findAll());
        
        return "librarian/update-quantity";
    }
    
    @GetMapping("/reservations")
    public String manageReservations(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        // Expirar reservaciones vencidas
        reservationService.expireOldReservations();
        
        return "librarian/reservations";
    }
    
    @PostMapping("/reservations/fulfill/{id}")
    public String fulfillReservation(@PathVariable Long id,
                                    HttpSession session,
                                    Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"LIBRARIAN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        
        try {
            reservationService.fulfillReservation(id);
            model.addAttribute("success", "Reservación procesada exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar reservación: " + e.getMessage());
        }
        
        return "redirect:/librarian/reservations";
    }
}