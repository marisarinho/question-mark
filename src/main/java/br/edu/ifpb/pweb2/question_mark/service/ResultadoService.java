package br.edu.ifpb.pweb2.question_mark.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.question_mark.model.Corrida;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.Resultado;
import br.edu.ifpb.pweb2.question_mark.repository.ResultadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ResultadoService {

    @Autowired
    ResultadoRepository resultadoRepository;

    @Autowired
    private CorridaService corridaService;

    public boolean existsById(Long id){
        return resultadoRepository.existsById(id);
    }

    public boolean existsByParticipanteAndCorrida(Participante participante, Corrida corrida){
        return resultadoRepository.existsByParticipanteAndCorrida(participante,corrida);


    }

    public Resultado save(Resultado resultado) {
        return resultadoRepository.save(resultado);
   
    }

    public List<Resultado> rankingGeral(){
        return resultadoRepository.findAllByOrderByPontuacaoDesc();
    }

    public List<Resultado> rankingCorrida(Long id){
        Corrida corrida = corridaService.findById(id);
        return resultadoRepository.findByCorridaOrderByPontuacaoDesc(corrida);
    }

    public boolean participanteTemResultado(Participante participante){
        return resultadoRepository.existsByParticipante(participante);
    }

    public void salvarResultado(Participante participante, Long id, Integer pontos) {
            Corrida corrida = corridaService.findById(id);
            Resultado res = new Resultado();
            res.setParticipante(participante);
            res.setCorrida(corrida);
            res.setPontuacao(new BigDecimal(pontos));
            res.setDataHora(LocalDateTime.now());
            resultadoRepository.save(res);
        }


        public Page<Resultado> rankingGeral(Pageable pageable) {
            return resultadoRepository.findAllByOrderByPontuacaoDesc(pageable);
        }

        public Page<Resultado> rankingCorrida(Long id, Pageable pageable) {
            Corrida corrida = corridaService.findById(id);
            return resultadoRepository.findByCorridaOrderByPontuacaoDesc(corrida, pageable);
        }
}
