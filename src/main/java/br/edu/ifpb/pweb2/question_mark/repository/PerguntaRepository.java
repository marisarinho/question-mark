package br.edu.ifpb.pweb2.question_mark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.question_mark.model.Pergunta;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
    
    List<Pergunta> findByCorridaId(Long corridaId);
    
}