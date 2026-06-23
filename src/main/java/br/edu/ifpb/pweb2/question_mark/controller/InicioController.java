package br.edu.ifpb.pweb2.question_mark.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String redirecionarInicio(Principal principal) {
        if (principal instanceof Authentication authentication
                && authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/corridas";
        }

        return "redirect:/home";
    }
}
