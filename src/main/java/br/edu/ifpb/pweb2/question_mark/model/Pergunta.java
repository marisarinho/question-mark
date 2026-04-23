package br.edu.ifpb.pweb2.question_mark.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Pergunta {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ElementCollection
private List<String> alternativas;    
private String titulo;
private Integer respostaCorreta;

    @ManyToOne
    @JoinColumn(name = "corrida_id")
    private Corrida corrida;

}
