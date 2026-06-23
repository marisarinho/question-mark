package br.edu.ifpb.pweb2.question_mark.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.pweb2.question_mark.exception.ArquivoInvalidoException;
import br.edu.ifpb.pweb2.question_mark.model.NivelDificuldade;
import br.edu.ifpb.pweb2.question_mark.model.Pergunta;
import br.edu.ifpb.pweb2.question_mark.repository.PerguntaRepository;

@ExtendWith(MockitoExtension.class)
class PerguntaServiceTests {

    @Mock
    private PerguntaRepository perguntaRepository;

    @InjectMocks
    private PerguntaService perguntaService;

    @BeforeEach
    void configurarLimite() {
        ReflectionTestUtils.setField(perguntaService, "tamanhoMaximoImagem", 10L);
    }

    @Test
    void deveSalvarImagemValidaNaPergunta() {
        Pergunta pergunta = novaPergunta();
        byte[] conteudo = {1, 2, 3};
        MockMultipartFile imagem = new MockMultipartFile(
                "imagem", "mapa.png", "image/png", conteudo);

        perguntaService.savePergunta(pergunta, imagem);

        assertArrayEquals(conteudo, pergunta.getImagem());
        verify(perguntaRepository).save(pergunta);
    }

    @Test
    void naoDeveSalvarImagemMaiorQueLimite() {
        Pergunta pergunta = novaPergunta();
        MockMultipartFile imagem = new MockMultipartFile(
                "imagem", "grande.png", "image/png", new byte[11]);

        assertThrows(ArquivoInvalidoException.class,
                () -> perguntaService.savePergunta(pergunta, imagem));
        verify(perguntaRepository, never()).save(pergunta);
    }

    @Test
    void naoDeveSalvarArquivoQueNaoSejaImagem() {
        Pergunta pergunta = novaPergunta();
        MockMultipartFile arquivo = new MockMultipartFile(
                "imagem", "texto.txt", "text/plain", "texto".getBytes());

        assertThrows(ArquivoInvalidoException.class,
                () -> perguntaService.savePergunta(pergunta, arquivo));
        verify(perguntaRepository, never()).save(pergunta);
    }

    @Test
    void deveRemoverImagemNaEdicao() {
        Pergunta pergunta = novaPergunta();
        pergunta.setId(1L);
        pergunta.setImagem(new byte[] {1});
        pergunta.setImagemNome("antiga.png");
        pergunta.setImagemTipo("image/png");
        when(perguntaRepository.findById(1L)).thenReturn(Optional.of(pergunta));

        perguntaService.atualizarPergunta(1L, novaPergunta(), null, true);

        assertNull(pergunta.getImagem());
        assertNull(pergunta.getImagemNome());
        assertNull(pergunta.getImagemTipo());
        verify(perguntaRepository).save(pergunta);
    }

    private Pergunta novaPergunta() {
        Pergunta pergunta = new Pergunta();
        pergunta.setEnunciado("Qual é a resposta?");
        pergunta.setAlternativas(List.of("A", "B", "C", "D"));
        pergunta.setRespostaCorreta(0);
        pergunta.setNivel(NivelDificuldade.FACIL);
        return pergunta;
    }
}
