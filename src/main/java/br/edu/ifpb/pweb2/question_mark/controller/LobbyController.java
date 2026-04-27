
//nao sabia se seria home ou lobby enfim pode mudar 

package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LobbyController {

    @Autowired
    private CorridaService corridaService; 

    @GetMapping("/home")
    public String exibirLobby(HttpSession session, Model model) {
        
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        
        
        if (participante == null) {
            return "redirect:/login"; 
        }

        
        model.addAttribute("nomeParticipante", participante.getNome());

        
        model.addAttribute("corridasAtivas", corridaService.findByAtivas(true));

      
        return "home"; 
    }
}