package br.edu.ifpb.pweb2.question_mark.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.question_mark.model.Participante;


@Repository
public interface ParticipanteRepository extends JpaRepository<Participante,Long>{
    Participante findByUserUsername(String username);
}