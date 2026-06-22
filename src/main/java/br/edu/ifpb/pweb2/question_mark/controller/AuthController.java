package br.edu.ifpb.pweb2.question_mark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.edu.ifpb.pweb2.question_mark.model.Authority;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.User;
import br.edu.ifpb.pweb2.question_mark.repository.ParticipanteRepository;
import br.edu.ifpb.pweb2.question_mark.repository.UserRepository;
import br.edu.ifpb.pweb2.question_mark.service.ParticipanteService;
import jakarta.servlet.http.HttpSession;




@Controller
@RequestMapping("")
public class AuthController {
    

    @Autowired
    private ParticipanteService participanteService;

    @Autowired 
    private ParticipanteRepository participanteRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String exibirTelaLogin() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String exibirTelaCadastro() {
        return "auth/cadastro";
    }

   @PostMapping("/cadasto")
    public String realizarCadastro(String username, String password, String email, RedirectAttributes redirect) {
        
        if (userRepository.existsById(username)) {
            redirect.addFlashAttribute("erro", "Esse nome já está em uso.");
            return "redirect:/cadastro";
        }

        User novoUsuario = new User();
        novoUsuario.setUsername(username);
        novoUsuario.setPassword(passwordEncoder.encode(password)); 
        novoUsuario.setEnabled(true);

        Authority authority = new Authority();
        authority.setId(new Authority.AuthorityId(username, "ROLE_PARTICIPANTE"));
        authority.setAuthority("ROLE_PARTICIPANTE");
        novoUsuario.setAuthorities(List.of(authority));

        userRepository.save(novoUsuario);

        Participante participante = new Participante();
        participante.setNome(username);
        participanteRepository.save(participante);

        redirect.addFlashAttribute("mensagem", "Conta criada com sucesso! Faça login para jogar.");
        return "redirect:/login";
    }

   @GetMapping("/acesso-negado")
    public String getAcessoNegado() {
        return "auth/acesso-negado"; 
    }
    
    
}
