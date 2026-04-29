package br.edu.ifpb.pweb2.question_mark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.repository.PerguntaRepository;

@Service
public class PerguntaService {

        /* 
        public Corrida atualizarPergunta(Long id, Long CorridaId, Long PerguntaId){
        Corrida lista_perguntas = perguntaRepository.findByCorridaId(CorridaId).orElse(null);
        Pergunta pergunta = perguntaRepository.findBy(PerguntaId).orElse(null);
        if (pergunta!= null) {
            pergunta.setEnunciado(pergunta.getEnunciado());
            pergunta.setRespostaCorreta(pergunta.getRespostaCorreta());
            pergunta.setAlternativas(pergunta.getAlternativas());
            pergunta.setNivel(pergunta.getNivel());
            return perguntaRepository.save(pergunta);
        }
        return pergunta;

        }
        */
}
