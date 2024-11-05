package br.com.fiap.sprint4.controller;

import br.com.fiap.sprint4.model.Usuario;
import br.com.fiap.sprint4.repository.UsuarioRepository;
import br.com.fiap.sprint4.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService; // Seu serviço para geração de tokens JWT

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login") // Método GET para exibir a página de login
    public String mostrarLogin(Model model) {
        return "login"; // Retorna a view login.html
    }

    @GetMapping("/cadastro") // Método GET para exibir a página de cadastro
    public String mostrarCadastro(Model model) {
        model.addAttribute("usuario", new Usuario()); // Adiciona um novo usuário ao modelo
        return "cadastro"; // Retorna a view cadastro.html
    }

    @PostMapping("/cadastro") // Método POST para cadastro de novo usuário
    public String cadastrar(@Valid @ModelAttribute Usuario usuario, Model model) {
        // Lógica para verificar se o usuário já existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("erro", "Email já cadastrado."); // Mensagem de erro
            return "cadastro"; // Retorna à página de cadastro com mensagem de erro
        }

        usuarioRepository.save(usuario); // Salva o novo usuário no banco
        return "redirect:/auth/login"; // Redireciona para a página de login
    }

    @PostMapping("/login") // Método POST para autenticação
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        try {
            // Autenticar o usuário
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // Pode lançar BadCredentialsException

            // Gerar o token
            String token = tokenService.gerarToken((Usuario) authentication.getPrincipal()); // Use o objeto autenticado
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas"); // Credenciais incorretas
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno do servidor"); // Erro geral
        }
    }
}
