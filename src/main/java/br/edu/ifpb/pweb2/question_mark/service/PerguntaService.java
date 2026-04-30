package br.edu.ifpb.pweb2.question_mark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.repository.PerguntaRepository;

@Service
public class PerguntaService {

    @Autowired
    PerguntaRepository perguntaRepository;

        
    public Pergunta atualizarPergunta(Long perguntaId, Pergunta perguntaEditada){
                            
                Pergunta pergunta = perguntaRepository.findById(perguntaId).get();

                pergunta.setEnunciado(perguntaEditada.getEnunciado());
                pergunta.setRespostaCorreta(perguntaEditada.getRespostaCorreta());
                pergunta.setAlternativas(perguntaEditada.getAlternativas());
                pergunta.setNivel(perguntaEditada.getNivel());
                
                return perguntaRepository.save(pergunta);
}

    
    public List<Pergunta> findByCorridaId(Long corridaId) {
        return perguntaRepository.findByCorridaId(corridaId);
    }

    public Pergunta findById(Long id){
            return perguntaRepository.findById(id).orElse(null);
    }
    
    
    public void deletarPergunta(Long id){
            if (perguntaRepository.existsById(id)) {
                perguntaRepository.deleteById(id);
            }
        

    }

    public Pergunta savePergunta(Pergunta pergunta){
        return perguntaRepository.save(pergunta);
    
    }
}
