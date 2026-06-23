package br.edu.ifpb.pweb2.question_mark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EstadoCorridaInvalidoException extends RuntimeException {

    public EstadoCorridaInvalidoException(String mensagem) {
        super(mensagem);
    }
}
