package br.edu.ifpb.pweb2.question_mark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.pweb2.question_mark.exception.ArquivoInvalidoException;
import br.edu.ifpb.pweb2.question_mark.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.repository.PerguntaRepository;

@Service
public class PerguntaService {

    @Autowired
    PerguntaRepository perguntaRepository;

    @Value("${app.upload.max-size-bytes}")
    private long tamanhoMaximoImagem;

        
    public Pergunta atualizarPergunta(Long perguntaId, Pergunta perguntaEditada,
            MultipartFile imagem, boolean excluirImagem){
                            
                Pergunta pergunta = findById(perguntaId);

                pergunta.setEnunciado(perguntaEditada.getEnunciado());
                pergunta.setRespostaCorreta(perguntaEditada.getRespostaCorreta());
                pergunta.setAlternativas(perguntaEditada.getAlternativas());
                pergunta.setNivel(perguntaEditada.getNivel());

                if (excluirImagem) {
                    removerImagem(pergunta);
                }
                if (imagem != null && !imagem.isEmpty()) {
                    salvarImagem(pergunta, imagem);
                }
                
                return perguntaRepository.save(pergunta);
}

    
    public List<Pergunta> findByCorridaId(Long corridaId) {
        return perguntaRepository.findByCorridaId(corridaId);
    }

    public Pergunta findById(Long id){
            return perguntaRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Pergunta não encontrada."));
    }
    
    
    public void deletarPergunta(Long id){
            if (perguntaRepository.existsById(id)) {
                perguntaRepository.deleteById(id);
            }
        

    }

    public Pergunta savePergunta(Pergunta pergunta, MultipartFile imagem){
        if (imagem != null && !imagem.isEmpty()) {
            salvarImagem(pergunta, imagem);
        }
        return perguntaRepository.save(pergunta);
    
    }

    private void salvarImagem(Pergunta pergunta, MultipartFile imagem) {
        if (imagem.getSize() > tamanhoMaximoImagem) {
            throw new ArquivoInvalidoException("A imagem ultrapassa o tamanho máximo permitido.");
        }

        String tipo = imagem.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new ArquivoInvalidoException("O arquivo enviado precisa ser uma imagem.");
        }

        try {
            pergunta.setImagem(imagem.getBytes());
            pergunta.setImagemNome(imagem.getOriginalFilename());
            pergunta.setImagemTipo(tipo);
        } catch (Exception exception) {
            throw new ArquivoInvalidoException("Não foi possível ler a imagem enviada.");
        }
    }

    private void removerImagem(Pergunta pergunta) {
        pergunta.setImagem(null);
        pergunta.setImagemNome(null);
        pergunta.setImagemTipo(null);
    }

    public Pergunta getPerguntaPorIndice(Long corridaId, int indice) {
        List<Pergunta> lista_perguntas = findByCorridaId(corridaId);
        if (indice < 0 || indice >= lista_perguntas.size()) {
            return null;
        }
        return lista_perguntas.get(indice);
    }
    public int contarPerguntasPorCorrida(Long corridaId){
        List<Pergunta> lista_perguntas = findByCorridaId(corridaId);
        return lista_perguntas.size();
    }
    
    public boolean verificarResposta(Pergunta pergunta, Integer resposta){
        return resposta.equals(pergunta.getRespostaCorreta());

    }

    public Integer calcularPontos(Pergunta pergunta, Integer pontosAtuais){
        return pontosAtuais + pergunta.getNivel().getPontos();
    }

    public Integer pontoPergunta(Pergunta pergunta){
        return pergunta.getNivel().getPontos();
    }
    
    public String getTextoRespostaCorreta(Pergunta pergunta) {
        return pergunta.getAlternativas().get(pergunta.getRespostaCorreta());
    }
}
