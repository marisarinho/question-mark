
//nao sabia se seria home ou lobby enfim pode mudar 

package br.edu.ifpb.pweb2.question_mark.controller;

import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.hibernate.mapping.List;
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
import br.edu.ifpb.pweb2.question_mark.service.ResultadoService;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/home")
public class LobbyController {

    private final PerguntaService perguntaService;

    private final PerguntaController perguntaController;

    @Autowired
    private CorridaService corridaService; 

    @Autowired
    ResultadoService resultadoService;

    LobbyController(PerguntaController perguntaController, PerguntaService perguntaService) {
        this.perguntaController = perguntaController;
        this.perguntaService = perguntaService;
    }

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

    @GetMapping("/corridas/{id}/iniciar")
        public String iniciarCorrida(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
            Participante participante = (Participante) session.getAttribute("participanteLogado");
            Corrida corrida = corridaService.findById(id);

            if (session.getAttribute("corridaId")!=null) {
                    redirect.addFlashAttribute("mensagem", "corrida em andamento");
                    return "redirect:/home";
                }

            if(resultadoService.existsByParticipanteAndCorrida(participante,corrida )){
                redirect.addFlashAttribute("mensagem","Participante ja jogou essa corrida");
                return "redirect:/home";
            };
                
                session.setAttribute("corridaId", id);
                session.setAttribute("indicePergunta",0);
                session.setAttribute("pontoAtual",0);
                session.setAttribute("inicioCorrida", LocalDateTime.now());
                return "redirect:/home/corrida/" + id + "/perguntas"; // tem que ser mesma url da exibição das perguntas

        }

        @GetMapping("/corridas/{id}/pergunta")
        public String corrida(@PathVariable Long id, HttpSession session, RedirectAttributes redirect){
            Long corridaId = (Long) session.getAttribute("corridaId");
            if (corridaId==null) {
                    redirect.addFlashAttribute("mensagem", "Nenhuma corrida em andamento");
                    return "redirect:/home";
                }

            LocalDateTime inicio = (LocalDateTime ) session.getAttribute("inicioCorrida");
            if(corridaService.tempoEstourado(id, inicio)){
                redirect.addFlashAttribute("mensagem","Tempo esgotado");
                return "redirect:/corrida/" + id +"/resultado";
            }
            int indice = (int) session.getAttribute("indicePergunta");
            Pergunta pergunta = perguntaService.getPerguntaPorIndice(corridaId, indice);
            if(pergunta == null){
                return "redirect:/corrida/" + id +"/resultado";
            }
            redirect.addAttribute("pergunta",pergunta);
            return "redirect:/corrida/" + id +"/resultado";
            

        }
        
        
    }