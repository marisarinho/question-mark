package br.edu.ifpb.pweb2.question_mark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ModelAndView tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception,
            HttpServletRequest request, HttpServletResponse response) {
        return criarPaginaErro(
                HttpStatus.NOT_FOUND,
                "🔎 Não encontramos isso",
                exception.getMessage(),
                request,
                response);
    }

    @ExceptionHandler({ArquivoInvalidoException.class, MaxUploadSizeExceededException.class})
    public ModelAndView tratarArquivoInvalido(Exception exception,
            HttpServletRequest request, HttpServletResponse response) {
        return criarPaginaErro(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "🖼️ Essa imagem ficou grandona demais",
                "Escolha uma imagem válida com até 2 MB e tente novamente.",
                request,
                response);
    }

    @ExceptionHandler(EstadoCorridaInvalidoException.class)
    public ModelAndView tratarEstadoCorridaInvalido(EstadoCorridaInvalidoException exception,
            HttpServletRequest request, HttpServletResponse response) {
        return criarPaginaErro(
                HttpStatus.CONFLICT,
                "🏁 Essa corrida já não está em andamento",
                exception.getMessage(),
                request,
                response);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    public ModelAndView tratarRequisicaoInvalida(Exception exception,
            HttpServletRequest request, HttpServletResponse response) {
        return criarPaginaErro(
                HttpStatus.BAD_REQUEST,
                "🤔 Não conseguimos entender esse pedido",
                "Confira os dados informados e tente outra vez.",
                request,
                response);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView tratarErroInesperado(Exception exception,
            HttpServletRequest request, HttpServletResponse response) {
        return criarPaginaErro(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "💜 Ops! O Question Mark tropeçou",
                "Aconteceu algo inesperado. Você pode voltar e tentar novamente.",
                request,
                response);
    }

    private ModelAndView criarPaginaErro(HttpStatus status, String titulo, String mensagem,
            HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(status.value());

        ModelAndView pagina = new ModelAndView("erro/erro");
        pagina.addObject("status", status.value());
        pagina.addObject("tituloErro", titulo);
        pagina.addObject("mensagemErro", mensagem);
        pagina.addObject("caminho", request.getRequestURI());
        return pagina;
    }
}
