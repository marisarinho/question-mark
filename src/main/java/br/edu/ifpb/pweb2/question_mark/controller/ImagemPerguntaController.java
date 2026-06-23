package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.question_mark.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.service.PerguntaService;

@Controller
@RequestMapping("/perguntas")
public class ImagemPerguntaController {

    private final PerguntaService perguntaService;

    public ImagemPerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> exibirImagem(@PathVariable Long id) {
        Pergunta pergunta = perguntaService.findById(id);
        if (pergunta.getImagem() == null) {
            throw new RecursoNaoEncontradoException("Esta pergunta não possui imagem.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pergunta.getImagemTipo()))
                .cacheControl(CacheControl.noCache())
                .body(pergunta.getImagem());
    }
}
