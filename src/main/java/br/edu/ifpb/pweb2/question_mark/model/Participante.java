package br.edu.ifpb.pweb2.question_mark.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter

public class Participante {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    private Boolean admin;

    @OneToOne(cascade = CascadeType.ALL) // nao sei se vai ser o all aqui
    @JoinColumn(name = "username")
    private User user;
   
}
