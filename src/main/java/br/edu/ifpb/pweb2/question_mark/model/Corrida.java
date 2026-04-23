package br.edu.ifpb.pweb2.question_mark.model;
import jakarta.persistence.CascadeType;
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
    private Long corrida_id;
    private String titulo;
    private String descricao;
    private Integer tempoSegundos;
    private Boolean ativa;


    @OneToMany(mappedBy ="corrida",cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;

}
