/*package br.edu.ifpb.pweb2.question_mark.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.edu.ifpb.pweb2.question_mark.model.Participante;

@Component
public class AuthInteceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception{
        Participante participante = (Participante) request.getSession().getAttribute("participanteLogado");

        if(participante==null){
            response.sendRedirect("/cadastro");
            return false;
        }
        return true;
    }
}
*/