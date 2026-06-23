package br.edu.ifpb.pweb2.question_mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.question_mark.model.Authority;
import br.edu.ifpb.pweb2.question_mark.model.Participante;
import br.edu.ifpb.pweb2.question_mark.model.User;
import br.edu.ifpb.pweb2.question_mark.repository.AuthorityRepository;
import br.edu.ifpb.pweb2.question_mark.repository.ParticipanteRepository;
import br.edu.ifpb.pweb2.question_mark.repository.UserRepository;

@Controller
@RequestMapping("")
public class AuthController {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

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

    @Transactional
    @PostMapping("/cadastro")
    public String realizarCadastro(String username, String password, RedirectAttributes redirect) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            redirect.addFlashAttribute("erro", "Informe usuario e senha para criar a conta.");
            return "redirect:/cadastro";
        }

        username = username.trim();

        if (userRepository.existsById(username)) {
            redirect.addFlashAttribute("erro", "Esse nome ja esta em uso.");
            return "redirect:/cadastro";
        }

        User novoUsuario = new User();
        novoUsuario.setUsername(username);
        novoUsuario.setPassword(passwordEncoder.encode(password));
        novoUsuario.setEnabled(true);

        Authority authority = new Authority();
        authority.setId(new Authority.AuthorityId(username, "ROLE_PARTICIPANTE"));
        authority.setAuthority("ROLE_PARTICIPANTE");

        userRepository.save(novoUsuario);
        authorityRepository.save(authority);

        Participante participante = new Participante();
        participante.setUser(novoUsuario);
        participanteRepository.save(participante);

        redirect.addFlashAttribute("mensagem", "Conta criada com sucesso! Faca login para jogar.");
        return "redirect:/login";
    }

    @GetMapping("/acesso-negado")
    public String getAcessoNegado() {
        return "auth/acesso-negado";
    }

}
