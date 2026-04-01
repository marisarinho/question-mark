package br.edu.ifpb.pweb2.question_mark.model;

import java.util.List;

import jakarta.persistence.Entity;

@Entity
public class Pergunta {
    private long id;
    private String titulo;
    private Integer respostaCorreta;
    private List<String> alternativas;
    private Corrida corrida;
}
