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
                
        session.setAttribute("corridaId", id);
        session.setAttribute("inicioCorrida", LocalDateTime.now());
        session.setAttribute("indicePergunta", 0);
        session.setAttribute("pontuacaoAtual", 0);

        
        return "redirect:/corridas/" + id + "/pergunta";
    }

    @GetMapping("/{id}/pergunta")
 
    public String exibirPergunta(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Integer indice = (Integer) session.getAttribute("indicePergunta");
        LocalDateTime inicio = (LocalDateTime) session.getAttribute("inicioCorrida");

        if (indice == null || corridaService.tempoEstourado(id, inicio)) {
        
            return finalizarCorrida(session, "Tempo esgotado!", redirectAttributes);
        }

        Corrida corrida = corridaService.findById(id);
        List<Pergunta> perguntas = corrida.getPerguntas();

        if (indice >= perguntas.size()) {
           
            return finalizarCorrida(session, "Corrida concluída!", redirectAttributes);
        }

        model.addAttribute("pergunta", perguntas.get(indice));
        model.addAttribute("totalPerguntas", perguntas.size());
        model.addAttribute("indiceAtual", indice + 1);
        long segundosPassados = java.time.temporal.ChronoUnit.SECONDS.between(inicio, LocalDateTime.now());
        long tempoRestante = corrida.getTempoSegundos() - segundosPassados;
        model.addAttribute("tempoRestante", tempoRestante);

        model.addAttribute("corridaId", id);

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

        if (corridaService.tempoEstourado(id, inicio)) {
           
            return finalizarCorrida(session, "Tempo esgotado!", redirect);
        }

        Pergunta perguntaAtual = perguntaService.getPerguntaPorIndice(corridaId,indice);


        if (perguntaAtual==null) {
            return finalizarCorrida(session, "Você terminou a corrida!", redirect);
        }

      boolean acertou = perguntaService.verificarResposta(perguntaAtual, resposta);

    if (acertou) {
            Integer pontosAtuais = (Integer) session.getAttribute("pontuacaoAtual");
            session.setAttribute("pontuacaoAtual", perguntaService.calcularPontos(perguntaAtual, pontosAtuais));
            redirect.addFlashAttribute("acertou", true);
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
            indice++;
            session.setAttribute("indicePergunta", indice);

            Pergunta perguntaAtual = perguntaService.getPerguntaPorIndice(corridaId, indice);

            if (perguntaAtual == null) {
                return finalizarCorrida(session, "Você terminou a corrida!", redirect);
            }

            return "redirect:/corridas/" + id + "/pergunta";
        }


    private String finalizarCorrida(HttpSession session, String mensagem, RedirectAttributes redirectAttributes) {
        Long corridaId = (Long) session.getAttribute("corridaId");
        Participante participante = (Participante) session.getAttribute("participanteLogado");
        Integer pontos = (Integer) session.getAttribute("pontuacaoAtual");

        if (pontos == null) pontos = 0;

        if (participante != null && corridaId != null) {
            Resultado res = new Resultado();
            res.setParticipante(participante);
            res.setCorrida(corridaService.findById(corridaId));
            res.setPontuacao(new BigDecimal(pontos));
            res.setDataHora(LocalDateTime.now());
            resultadoService.save(res);
        }

        session.removeAttribute("indicePergunta");
        session.removeAttribute("pontuacaoAtual");
        session.removeAttribute("inicioCorrida");
        session.removeAttribute("corridaId");
        
        redirectAttributes.addFlashAttribute("pontosFinais", pontos);
        redirectAttributes.addFlashAttribute("corridaId", corridaId);
        redirectAttributes.addFlashAttribute("mensagemFinal", mensagem); 
        
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