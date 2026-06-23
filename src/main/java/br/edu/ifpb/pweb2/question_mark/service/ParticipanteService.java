package br.edu.ifpb.pweb2.question_mark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.User;
import br.edu.ifpb.pweb2.question_mark.repository.ParticipanteRepository;
import br.edu.ifpb.pweb2.question_mark.repository.UserRepository;
import br.edu.ifpb.pweb2.question_mark.exception.RecursoNaoEncontradoException;

@Service
public class ParticipanteService {
 
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    ParticipanteRepository participanteRepository;

  public Participante logarParticipante(String username) {
    Participante participanteEncontrado = participanteRepository.findByUserUsername(username);

    if (participanteEncontrado == null) {
        participanteEncontrado = new Participante();

        User user = userRepository.findById(username)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
        participanteEncontrado.setUser(user);

        participanteRepository.save(participanteEncontrado);
    }

    return participanteEncontrado;
}
}
