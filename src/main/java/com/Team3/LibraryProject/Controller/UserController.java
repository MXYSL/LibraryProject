package com.Team3.LibraryProject.Controller;

import com.Team3.LibraryProject.Service.UserService;
import com.Team3.LibraryProject.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "Debes iniciar sesión.");
            return "change-password";
        }
        boolean changed = userService.changePassword(user.getId(), oldPassword, newPassword);
        if (changed) {
            model.addAttribute("success", "Contraseña cambiada exitosamente.");
        } else {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
        }
        return "change-password";
    }
}