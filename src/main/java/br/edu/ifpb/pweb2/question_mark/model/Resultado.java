package br.edu.ifpb.pweb2.question_mark.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Resultado {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long resultado_id;

 
    @ManyToOne
    @JoinColumn(name = "corrida_id")
    private Corrida corrida;

    private BigDecimal pontuacao;
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;
}
