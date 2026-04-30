package br.edu.ifpb.pweb2.question_mark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NivelDificuldade {
    FACIL(10),
    MEDIO(20),
    DIFICIL(30);

    private final int pontos;
}