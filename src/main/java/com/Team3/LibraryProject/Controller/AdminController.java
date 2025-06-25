package com.Team3.LibraryProject.Controller;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private FineService fineService;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getUserType());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("totalPendingFines", fineService.getTotalPendingFines());
        model.addAttribute("overdueLoans", loanService.getOverdueLoans().size());
        return "admin/dashboard";
    }

    @GetMapping("/books")
    public String manageBooks(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("books", bookService.getAvailableBooks());
        model.addAttribute("genres", bookService.getAllGenres());
        return "admin/books";
    }

    @PostMapping("/books/add")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String publisher,
                          @RequestParam String edition,
                          @RequestParam int quantity,
                          @RequestParam String isbn,
                          @RequestParam String synopsis,
                          @RequestParam Long genreId,
                          @RequestParam String coverImageUrl,
                          @RequestParam String materialType,
                          HttpSession session,
                          Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        try {
            bookService.createBook(title, author, publisher, edition, quantity,
                    isbn, synopsis, genreId, coverImageUrl, materialType);
            model.addAttribute("success", "Libro agregado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al agregar libro: " + e.getMessage());
        }
        model.addAttribute("books", bookService.getAvailableBooks());
        model.addAttribute("genres", bookService.getAllGenres());
        return "admin/books";
    }

    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String author,
                           @RequestParam String publisher,
                           @RequestParam String edition,
                           @RequestParam String synopsis,
                           @RequestParam Long genreId,
                           @RequestParam String coverImageUrl,
                           HttpSession session,
                           Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        try {
            bookService.updateBook(id, title, author, publisher, edition,
                    synopsis, genreId, coverImageUrl);
            model.addAttribute("success", "Libro actualizado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar libro: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id,
                             HttpSession session,
                             Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        try {
            bookService.deleteBook(id);
            model.addAttribute("success", "Libro eliminado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar libro: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/librarians")
    public String manageLibrarians(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/librarians";
    }

    @PostMapping("/librarians/add")
    public String addLibrarian(@RequestParam String name,
                               @RequestParam int age,
                               @RequestParam String address,
                               @RequestParam String ineFolio,
                               @RequestParam String rfc,
                               @RequestParam String curp,
                               @RequestParam String maritalStatus,
                               @RequestParam String workSchedule,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        try {
            Librarian librarian = userService.createLibrarian(name, age, address,
                    ineFolio, rfc, curp, maritalStatus, workSchedule);
            librarian.setPassword(password);
            model.addAttribute("success", "Bibliotecario creado exitosamente. ID: " + librarian.getId());
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear bibliotecario: " + e.getMessage());
        }
        return "admin/librarians";
    }

    @GetMapping("/genres")
    public String manageGenres(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("genres", bookService.getAllGenres());
        return "admin/genres";
    }

    @PostMapping("/genres/add")
    public String addGenre(@RequestParam String name,
                           @RequestParam String description,
                           HttpSession session,
                           Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        try {
            bookService.createGenre(name, description);
            model.addAttribute("success", "Género agregado exitosamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al agregar género: " + e.getMessage());
        }
        model.addAttribute("genres", bookService.getAllGenres());
        return "admin/genres";
    }

    @GetMapping("/reports")
    public String reports(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("totalPendingFines", fineService.getTotalPendingFines());
        model.addAttribute("totalPendingFinesCount", fineService.getTotalPendingFinesCount());
        model.addAttribute("overdueLoansCount", loanService.getOverdueLoans().size());
        return "admin/reports";
    }
}