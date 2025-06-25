package com.Team3.LibraryProject.Controller;

import com.Team3.LibraryProject.Entity.User;
import com.Team3.LibraryProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String userId, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            Optional<User> user = userService.authenticateUser(userId, password);
            
            if (user.isPresent()) {
                session.setAttribute("user", user.get());
                
                // Redireccionar según el tipo de usuario
                String userType = user.get().getUserType();
                switch (userType) {
                    case "ADMIN":
                        return "redirect:/admin/dashboard";
                    case "LIBRARIAN":
                        return "redirect:/librarian/dashboard";
                    case "READER":
                        return "redirect:/reader/dashboard";
                    default:
                        model.addAttribute("error", "Tipo de usuario no válido");
                        return "login";
                }
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error en el sistema: " + e.getMessage());
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String name,
                          @RequestParam String birthDate,
                          Model model) {
        try {
            // Crear nuevo lector con contraseña y credencial generadas
            var reader = userService.createReader(
                name,
                java.time.LocalDate.parse(birthDate)
            );
            model.addAttribute("success", "Usuario registrado exitosamente. ID: " + reader.getId() +
                "<br>Credencial: " + reader.getLibraryCredential() +
                "<br>Contraseña temporal: " + reader.getPassword() +
                "<br>Por favor, cambia tu contraseña después de iniciar sesión.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "register";
        }
    }
}