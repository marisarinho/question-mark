package br.edu.ifpb.pweb2.question_mark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.question_mark.model.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
    
    List<Pergunta> findByCorridaId(Long corridaId);
}