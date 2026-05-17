package br.edu.ifpb.pweb2.question_mark.model;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Corrida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private Integer tempoSegundos;
    private Boolean ativa;

    @OneToMany(mappedBy = "corrida", cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;

    @OneToMany(mappedBy = "corrida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resultado> resultados;
}
