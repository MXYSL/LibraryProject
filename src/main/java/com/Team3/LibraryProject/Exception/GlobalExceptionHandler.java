package com.Team3.LibraryProject.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserLimitException.class)
    public String handleUserLimitException(UserLimitException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/librarians";
    }
    
    @ExceptionHandler(BookNotAvailableException.class)
    public String handleBookNotAvailableException(BookNotAvailableException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reader/catalog";
    }
    
    @ExceptionHandler(FinesPendingException.class)
    public String handleFinesPendingException(FinesPendingException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reader/fines";
    }
    
    @ExceptionHandler(LibraryException.class)
    public String handleLibraryException(LibraryException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("error", "Ha ocurrido un error inesperado: " + e.getMessage());
        return "error";
    }
}
