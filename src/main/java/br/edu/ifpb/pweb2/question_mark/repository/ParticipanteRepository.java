package br.edu.ifpb.pweb2.question_mark.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.question_mark.model.Participante;


public interface ParticipanteRepository extends JpaRepository<Participante,Long>{
    public Participante findByEmail(String email);
}