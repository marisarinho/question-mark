package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.models.Participante;
import br.edu.ifpb.pweb2.question_mark.service.ParticipanteService;
import jakarta.servlet.http.HttpSession;



@Controller
public class AuthController {
    

    @Autowired
    private ParticipanteService ParticipanteService;


    @GetMapping("/login")
    public String exibirTelaLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String formLogin(@RequestParam String nome, 
                            @RequestParam String email, 
                            HttpSession session, 
                            RedirectAttributes redirect) {

        Participante participante = ParticipanteService.logarParticipante(nome,email);

        session.setAttribute("participanteLogado", participante);

        redirect.addFlashAttribute("Mensagem","Usuario Logado com sucesso");

        if (participante.getAdmin()) {  
            return "redirect:/admin";
        }
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession, RedirectAttributes redirect) {
        httpSession.invalidate();
        redirect.addFlashAttribute("mensagem","logout feto com sucesso");
        return "redirect:/auth/login";
    }
    
    
}
