package br.edu.ifpb.pweb2.question_mark.repository;
import  br.edu.ifpb.pweb2.question_mark.model.Corrida;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CorridaRepository extends JpaRepository<Corrida,Long>{
    public List<Corrida> findByAtiva(boolean ativa);
}
