
//nao sabia se seria home ou lobby enfim pode mudar 

package br.edu.ifpb.pweb2.question_mark.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;
import br.edu.ifpb.pweb2.question_mark.service.ResultadoService;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/home")
public class LobbyController {



    @Autowired
    private CorridaService corridaService; 

    @Autowired
    ResultadoService resultadoService;

    
    @GetMapping()
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