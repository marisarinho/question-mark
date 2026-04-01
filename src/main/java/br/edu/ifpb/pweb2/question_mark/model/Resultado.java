package br.edu.ifpb.pweb2.question_mark.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Entity
public class Resultado {
    private long id;
    private Participante participante;
    private Corrida corrida;
    private BigDecimal pontuacao;
    private LocalDateTime dataHora;

    
}
