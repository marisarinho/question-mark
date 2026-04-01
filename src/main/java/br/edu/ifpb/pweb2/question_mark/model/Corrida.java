package br.edu.ifpb.pweb2.question_mark.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Corrida {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private Integer tempoSegundos;
    private Boolean ativa;

    @OneToMany(mappedBy ="corrida")
    private List<Pergunta> perguntas;

}
