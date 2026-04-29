package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.NivelDificuldade;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.repository.PerguntaRepository;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;

import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin/corridas/{corridaId}/perguntas")
public class PerguntaController {

    @Autowired
    private PerguntaRepository perguntaRepository;

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;
    
    @GetMapping
    public String listar(@PathVariable Long corridaId, Model model) {
        Corrida corrida = corridaService.findById(corridaId);
        model.addAttribute("corrida", corrida);
        model.addAttribute("perguntas", perguntaRepository.findByCorridaId(corridaId));
        return "admin/perguntas/lista";
    }

   
    @GetMapping("/nova")
    public String exibirForm(@PathVariable Long corridaId, Model model) {
        Pergunta pergunta = new Pergunta();
        pergunta.setCorrida(corridaService.findById(corridaId));
        model.addAttribute("pergunta", pergunta);
        model.addAttribute("corridaId", corridaId);
        model.addAttribute("niveis", NivelDificuldade.values());
        return "admin/perguntas/form";
    }

    
    @PostMapping("/nova")
    public String salvar(@PathVariable Long corridaId, Pergunta pergunta, RedirectAttributes flash) {
        Corrida corrida = corridaService.findById(corridaId);
        pergunta.setCorrida(corrida);
        perguntaRepository.save(pergunta);
        flash.addFlashAttribute("mensagem", "Pergunta adicionada com sucesso!");
        return "redirect:/admin/corridas/" + corridaId + "/perguntas";
    }

    
        
    
    

    @PostMapping("/{perguntaId}/deletar")
    public String deletar(@PathVariable Long corridaId, @PathVariable Long perguntaId, RedirectAttributes flash) {
        perguntaRepository.deleteById(perguntaId);
        flash.addFlashAttribute("mensagem", "Pergunta removida!");
        return "redirect:/admin/corridas/" + corridaId + "/perguntas";
    }
}