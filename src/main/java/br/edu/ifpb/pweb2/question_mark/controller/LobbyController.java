
package br.edu.ifpb.pweb2.question_mark.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import br.edu.ifpb.pweb2.question_mark.service.ParticipanteService;


@Controller
@RequestMapping("/home")
@PreAuthorize("hasRole('PARTICIPANTE')")
public class LobbyController {

    @Autowired
    private CorridaService corridaService; 

    @Autowired
    private ParticipanteService participanteService;

    @GetMapping()
    public String home(Model model, Principal principal) {
        Participante participante = participanteService.logarParticipante(principal.getName());

        model.addAttribute("nomeParticipante", principal.getName());
        model.addAttribute("corridasAtivas", corridaService.findByAtivas(true));

        return "home";
    }
}
