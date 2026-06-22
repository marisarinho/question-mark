package br.edu.ifpb.pweb2.question_mark.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.exception.EstadoCorridaInvalidoException;
import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.model.Resultado;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;
import br.edu.ifpb.pweb2.question_mark.service.ResultadoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/corridas")
public class CorridaParticipanteController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private ResultadoService resultadoService; 

    @Autowired 
    private PerguntaService perguntaService;


    @GetMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Corrida corrida = corridaService.findById(id);
        Participante participante = (Participante) session.getAttribute("participanteLogado");

         if(resultadoService.existsByParticipanteAndCorrida(participante,corrida )){
                redirect.addFlashAttribute("mensagem","Participante ja jogou essa corrida");
                return "redirect:/home";
            };
            
        int totalPerguntas = perguntaService.contarPerguntasPorCorrida(id);

        if (totalPerguntas==0){
                redirect.addFlashAttribute("mensagem","Corrida sem perguntas");
                return "redirect:/home";
            }

        redirect.addFlashAttribute("mensagem","Corrida começou!!");


        session.setAttribute("corridaId", id);
        session.setAttribute("inicioCorrida", LocalDateTime.now());
        session.setAttribute("indicePergunta", 0);
        session.setAttribute("pontuacaoAtual", 0);

        
        return "redirect:/corridas/" + id + "/pergunta";
    }

        
    @GetMapping("/{id}/pergunta")
        public String exibirPergunta(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
            Long corridaId = (Long) session.getAttribute("corridaId");
            Integer indice = (Integer) session.getAttribute("indicePergunta");
            LocalDateTime inicio = (LocalDateTime) session.getAttribute("inicioCorrida");

            validarCorridaEmAndamento(id, corridaId, indice, inicio);

            if (corridaService.tempoEstourado(corridaId, inicio)) {
                return finalizarCorrida(session, "Tempo esgotado!", redirectAttributes);
            }

            

            Pergunta pergunta = perguntaService.getPerguntaPorIndice(corridaId, indice);

            if (pergunta == null) {
                return finalizarCorrida(session, "Corrida concluída!", redirectAttributes);
            }

            int totalPerguntas = perguntaService.contarPerguntasPorCorrida(corridaId);

            if (totalPerguntas==0){

            }

            model.addAttribute("pergunta", pergunta);
            model.addAttribute("totalPerguntas", totalPerguntas);
            model.addAttribute("indiceAtual", indice + 1);
            model.addAttribute("tempoRestante", corridaService.tempoRestanteEmSegundos(corridaId,inicio));
            model.addAttribute("corridaId", corridaId);

            return "participante/pergunta";
        }

    @PostMapping("/{id}/pergunta")
    public String processarResposta(@PathVariable Long id, 
                                   @RequestParam Integer resposta, 
                                   HttpSession session, 
                                   RedirectAttributes redirect) { 
        
        Long corridaId = (Long) session.getAttribute("corridaId");
        Integer indice = (Integer) session.getAttribute("indicePergunta");
        LocalDateTime inicio = (LocalDateTime) session.getAttribute("inicioCorrida");

        validarCorridaEmAndamento(id, corridaId, indice, inicio);

        if (corridaService.tempoEstourado(id, inicio)) {
           
            return finalizarCorrida(session, "Tempo esgotado!", redirect);
        }

        Pergunta perguntaAtual = perguntaService.getPerguntaPorIndice(corridaId,indice);


        if (perguntaAtual==null) {
            return finalizarCorrida(session, "Você terminou a corrida!", redirect);
        }

      boolean acertou = perguntaService.verificarResposta(perguntaAtual, resposta);
      Integer ponto = perguntaService.pontoPergunta(perguntaAtual);

    if (acertou) {
            Integer pontosAtuais = (Integer) session.getAttribute("pontuacaoAtual");
            session.setAttribute("pontuacaoAtual", perguntaService.calcularPontos(perguntaAtual, pontosAtuais));
            redirect.addFlashAttribute("acertou", true);
            redirect.addFlashAttribute("ponto",ponto);
        } else {
            redirect.addFlashAttribute("acertou", false);
            redirect.addFlashAttribute("respostaCorreta", perguntaService.getTextoRespostaCorreta(perguntaAtual));
        }

        return "redirect:/corridas/" + id + "/pergunta";
    }

   @PostMapping("/{id}/pergunta/proxima")
        public String proxima(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
            Long corridaId = (Long) session.getAttribute("corridaId");
            Integer indice = (Integer) session.getAttribute("indicePergunta");
            LocalDateTime inicio = (LocalDateTime) session.getAttribute("inicioCorrida");

            validarCorridaEmAndamento(id, corridaId, indice, inicio);

            indice++;
            session.setAttribute("indicePergunta", indice);

            Pergunta perguntaAtual = perguntaService.getPerguntaPorIndice(corridaId, indice);

            if (perguntaAtual == null) {
                return finalizarCorrida(session, "Você terminou a corrida!", redirect);
            }

            return "redirect:/corridas/" + id + "/pergunta";
        }

    private void validarCorridaEmAndamento(Long id, Long corridaId, Integer indice, LocalDateTime inicio) {
        if (corridaId == null || indice == null || inicio == null || !id.equals(corridaId)) {
            throw new EstadoCorridaInvalidoException(
                    "Volte para o início e escolha uma corrida para jogar.");
        }
    }


    private String finalizarCorrida(HttpSession session, String mensagem, RedirectAttributes redirectAttributes) {
        Long corridaId = (Long) session.getAttribute("corridaId");
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        Integer pontos = (Integer) session.getAttribute("pontuacaoAtual");

        if (pontos == null) pontos = 0;

        if (participante != null && corridaId != null) {
            resultadoService.salvarResultado(participante, corridaId, pontos);
        }

        session.removeAttribute("indicePergunta");
        session.removeAttribute("pontuacaoAtual");
        session.removeAttribute("inicioCorrida");
        session.removeAttribute("corridaId");
        
        redirectAttributes.addFlashAttribute("pontosFinais", pontos);
        redirectAttributes.addFlashAttribute("corridaId", corridaId);
        redirectAttributes.addFlashAttribute("mensagem", mensagem); 
        
        return "redirect:/corridas/resultado"; 
    }

    @GetMapping("/resultado")
    public String exibirResultadoIndividual() {
        return "participante/resultado";
    }


   @GetMapping("/ranking")
    public String exibirRanking(HttpSession session, Model model) {
        List<Resultado> ranking = resultadoService.rankingGeral();
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        Boolean temResultado = resultadoService.participanteTemResultado(participante);
        model.addAttribute("ranking", ranking);
        model.addAttribute("participanteLogado", participante);
        model.addAttribute("temResultado", temResultado);
        return "participante/ranking";
    }

    @GetMapping("/{id}/ranking")
    public String exibirRankingPorCorrida(@PathVariable Long id, HttpSession session, Model model) {
        Corrida corrida = corridaService.findById(id);
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        List<Resultado> ranking = resultadoService.rankingCorrida(id);
        Boolean temResultado = resultadoService.existsByParticipanteAndCorrida(participante, corrida);
        model.addAttribute("ranking", ranking);
        model.addAttribute("participanteLogado", participante);
        model.addAttribute("temResultado", temResultado);
        model.addAttribute("corrida", corrida);
        return "participante/rankingCorrida";
    }
}
