package br.edu.ifpb.pweb2.question_mark.repository;
import br.edu.ifpb.pweb2.question_mark.models.Participante;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipanteRepository extends JpaRepository<Participante,Long>{
    public Participante findByEmail(String email);
}