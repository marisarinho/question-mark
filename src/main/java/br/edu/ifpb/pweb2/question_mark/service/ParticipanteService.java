package br.edu.ifpb.pweb2.question_mark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.repository.ParticipanteRepository;

@Service
public class ParticipanteService {
 
    
    @Autowired
    ParticipanteRepository participanteRepository;

    public Participante logarParticipante(String nome){
        Participante participanteEncontrado = participanteRepository.findByNome(nome);
        if (participanteEncontrado==null){
            participanteEncontrado = new Participante();
            participanteEncontrado.setNome(nome);

            participanteEncontrado.setAdmin(false); 
            participanteRepository.save(participanteEncontrado);
        }
            return participanteEncontrado;
        


    }
}
