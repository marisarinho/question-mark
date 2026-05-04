package br.edu.ifpb.pweb2.question_mark.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter

public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enunciado; 
    private Integer respostaCorreta;


    @ElementCollection
    private List<String> alternativas;
    
    @Enumerated(EnumType.STRING)
    private NivelDificuldade nivel;

    @ManyToOne
    @JoinColumn(name = "corrida_id")
    private Corrida corrida;
//provisorio
    public Integer getRespostaCorreta() {
        return this.respostaCorreta;
    }

    public NivelDificuldade getNivel() {
        return this.nivel;
    }
}
