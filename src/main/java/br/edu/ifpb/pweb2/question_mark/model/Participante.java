package br.edu.ifpb.pweb2.question_mark.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity


public class Participante {

    @Id
    private long id;
    private String nome;
    private String email;
    private Boolean admin;

    @OneToMany(mappedBy = "participante")
    private List<Corrida> corridasFeitas;
}
