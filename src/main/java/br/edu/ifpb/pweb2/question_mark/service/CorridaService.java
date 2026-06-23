package br.edu.ifpb.pweb2.question_mark.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.exception.RecursoNaoEncontradoException;
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
        return corridaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Corrida não encontrada."));
    }


    public Corrida saveCorrida(Corrida corrida){
        validarCorrida(corrida);
        return corridaRepository.save(corrida);
    
    }
    
    public Corrida atualizarCorrida(Long id, Corrida corrida){
        validarCorrida(corrida);
        Corrida corridaEncontrada = findById(id);
        corridaEncontrada.setTitulo(corrida.getTitulo());
        corridaEncontrada.setDescricao(corrida.getDescricao());
        corridaEncontrada.setTempoSegundos(corrida.getTempoSegundos());
        corridaEncontrada.setAtiva(corrida.getAtiva());
        return corridaRepository.save(corridaEncontrada);

        }

    public void deletarCorrida(Long id){
        Corrida corrida = findById(id);
        corridaRepository.delete(corrida);
    }
    
    public List<Corrida> todasCorridas(){
        return corridaRepository.findAll();
    }

    public boolean tempoEstourado(Long corridaId, LocalDateTime inicioCorrida){
            Corrida corridaEcontrada = this.findById(corridaId);
            int tempoSegundos = corridaEcontrada.getTempoSegundos();
            long segundosPassados = ChronoUnit.SECONDS.between(inicioCorrida, LocalDateTime.now());
            return segundosPassados>=tempoSegundos;
        }

    private void validarCorrida(Corrida corrida) {
        if (corrida.getTitulo() == null || corrida.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Informe o titulo da corrida.");
        }

        if (corrida.getDescricao() == null || corrida.getDescricao().isBlank()) {
            throw new IllegalArgumentException("Informe a descricao da corrida.");
        }

        if (corrida.getTempoSegundos() == null || corrida.getTempoSegundos() <= 0) {
            throw new IllegalArgumentException("O tempo da corrida precisa ser maior que zero.");
        }

        if (corrida.getAtiva() == null) {
            corrida.setAtiva(false);
        }
    }
    public Integer tempoSegundos(Long corridaId){
        Corrida corrida = findById(corridaId);
        return corrida.getTempoSegundos();

    }

    public Integer tempoRestanteEmSegundos(Long corridaId, LocalDateTime inicioCorrida) {
        Corrida corrida = this.findById(corridaId);
        int tempoTotal = corrida.getTempoSegundos();
        long segundosPassados = ChronoUnit.SECONDS.between(inicioCorrida, LocalDateTime.now());
        
        int restante = (int) (tempoTotal - segundosPassados);
        
        if (restante > 0) {
            return restante;
        } else {
            return 0;
        }
    }
  
}
