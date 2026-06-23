package br.edu.ifpb.pweb2.question_mark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.Resultado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long>{
    boolean existsByParticipanteAndCorrida(Participante participante, Corrida corrida);
    List<Resultado> findAllByOrderByPontuacaoDesc();
    boolean existsByParticipante(Participante participante);
    List<Resultado> findByCorridaOrderByPontuacaoDesc(Corrida corrida);
    Page<Resultado> findAllByOrderByPontuacaoDesc(Pageable pageable);
    Page<Resultado> findByCorridaOrderByPontuacaoDesc(Corrida corrida, Pageable pageable);
    
}
