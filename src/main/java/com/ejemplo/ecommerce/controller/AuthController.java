package com.ejemplo.ecommerce.controller;

import com.ejemplo.ecommerce.model.User;
import com.ejemplo.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") User user,
                                 RedirectAttributes redirectAttrs,
                                 Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El email ya está registrado");
            return "register";
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_CLIENT");
        userRepository.save(user);
        
        redirectAttrs.addFlashAttribute("success", "Registro exitoso. Inicia sesión.");
        return "redirect:/login";
    }
}