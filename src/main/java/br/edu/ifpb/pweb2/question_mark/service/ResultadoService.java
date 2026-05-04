package br.edu.ifpb.pweb2.question_mark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.Resultado;
import br.edu.ifpb.pweb2.question_mark.repository.ResultadoRepository;

@Service
public class ResultadoService {

    @Autowired
    ResultadoRepository resultadoRepository;

    public boolean existsById(Long id){
        return resultadoRepository.existsById(id);
    }

    public boolean existsByParticipanteAndCorrida(Participante participante, Corrida corrida){
        return resultadoRepository.existsByParticipanteAndCorrida(participante,corrida);


    }

    public Resultado save(Resultado resultado) {
        return resultadoRepository.save(resultado);
   
    }
}
