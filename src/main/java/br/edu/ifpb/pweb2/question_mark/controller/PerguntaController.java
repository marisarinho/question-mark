package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.NivelDificuldade;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.exception.EstadoCorridaInvalidoException;
import br.edu.ifpb.pweb2.question_mark.service.CorridaService;
import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;


@Controller
@RequestMapping("/admin/corridas/{corridaId}/perguntas")
@PreAuthorize("hasRole('ADMIN')")
public class PerguntaController {



    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;

    
    
    @GetMapping
    public String listar(@PathVariable Long corridaId, Model model) {
        Corrida corrida = corridaService.findById(corridaId);
        model.addAttribute("corrida", corrida);
        model.addAttribute("perguntas", perguntaService.findByCorridaId(corridaId));
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
    public String salvar(@PathVariable Long corridaId, Pergunta pergunta,
            @RequestParam(name = "arquivoImagem", required = false) MultipartFile imagem,
            RedirectAttributes flash) {
        Corrida corrida = corridaService.findById(corridaId);
        pergunta.setCorrida(corrida);
        perguntaService.savePergunta(pergunta, imagem);
        flash.addFlashAttribute("mensagem", "Pergunta adicionada com sucesso!");
        return "redirect:/admin/corridas/" + corridaId + "/perguntas";
    }

    
    @GetMapping("/{id}/editar")
    public String exibirFormDaPergunta(@PathVariable Long corridaId, @PathVariable Long id, Model model) {
        Pergunta perguntaEncontrada = perguntaService.findById(id);
        validarPerguntaDaCorrida(corridaId, perguntaEncontrada);

        model.addAttribute("pergunta", perguntaEncontrada);
        model.addAttribute("corridaId", corridaId);
        model.addAttribute("niveis", NivelDificuldade.values());
        return "admin/perguntas/form";

    }
    
    @PostMapping("/{id}/editar")
        public String atualizarCorrida(
                @PathVariable Long corridaId,
                @PathVariable Long id,
                Pergunta pergunta,
                @RequestParam(name = "arquivoImagem", required = false) MultipartFile imagem,
                @RequestParam(defaultValue = "false") boolean excluirImagem,
                RedirectAttributes redirectAttributes) {
                validarPerguntaDaCorrida(corridaId, perguntaService.findById(id));
                perguntaService.atualizarPergunta(id, pergunta, imagem, excluirImagem);
                redirectAttributes.addFlashAttribute("mensagem", "Pergunta atualizada com sucesso!");
                return "redirect:/admin/corridas/" + corridaId + "/perguntas";

        }

    @PostMapping("/{perguntaId}/deletar")
    public String deletar(@PathVariable Long corridaId, @PathVariable Long perguntaId, RedirectAttributes flash) {
        validarPerguntaDaCorrida(corridaId, perguntaService.findById(perguntaId));
        perguntaService.deletarPergunta(perguntaId);
        flash.addFlashAttribute("mensagem", "Pergunta removida!");
        return "redirect:/admin/corridas/" + corridaId + "/perguntas";
    }

    private void validarPerguntaDaCorrida(Long corridaId, Pergunta pergunta) {
        if (pergunta.getCorrida() == null || !corridaId.equals(pergunta.getCorrida().getId())) {
            throw new EstadoCorridaInvalidoException("Pergunta nao pertence a corrida informada.");
        }
    }
}
