package br.edu.ifpb.pweb2.question_mark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.repository.CorridaRepository;

@Service
public class CorridaService {

    @Autowired
    private CorridaRepository corridaRepository;

    public List<Corrida> findByAtivas(boolean ativa){
        return corridaRepository.findByAtiva(ativa);
    }

    public Corrida findById(Long id){
        return corridaRepository.findById(id).orElse(null);
    }


    public Corrida saveCorrida(Corrida corrida){
        return corridaRepository.save(corrida);
    
    }
    
    public Corrida atualizarCorrida(Long id, Corrida corrida){
        Corrida corridaEncontrada = corridaRepository.findById(id).orElse(null);
        if (corridaEncontrada!= null) {
            corridaEncontrada.setTitulo(corrida.getTitulo());
            corridaEncontrada.setDescricao(corrida.getDescricao());
            corridaEncontrada.setTempoSegundos(corrida.getTempoSegundos());
            corridaEncontrada.setAtiva(corrida.getAtiva());
            return corridaRepository.save(corridaEncontrada);
        }
        return corridaEncontrada;

        }

    public void deletarCorrida(Long id){
            if (corridaRepository.existsById(id)) {
                corridaRepository.deleteById(id);
            }
        

    }
    
    public List<Corrida> todasCorridas(){
        return corridaRepository.findAll();
    }



}
