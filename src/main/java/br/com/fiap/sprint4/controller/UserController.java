package br.com.fiap.sprint4.controller;

import br.com.fiap.sprint4.model.Perfil;
import br.com.fiap.sprint4.model.Usuario;
import br.com.fiap.sprint4.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @GetMapping("/login")
    public String login() {
        return "login"; // Retorna a página de login
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        List<Perfil> perfis = Arrays.asList(Perfil.values());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfis", perfis);
        return "cadastro"; // Retorna a página de cadastro
    }

    @PostMapping("/login") // Endpoint para autenticação
    public String autenticar(@Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login"; // Se houver erros de validação, retorna à página de login
        }

        // Lógica de autenticação
        if (!autenticacaoService.autenticar(usuario.getEmail(), usuario.getSenha())) {
            model.addAttribute("erro", "Usuário não cadastrado. Clique em 'Cadastre-se' para criar uma conta.");
            return "login"; // Retorna à página de login com mensagem de erro
        }

        return "redirect:/index"; // Redireciona para a página index após autenticação
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cadastro"; // Se houver erros de validação, retorna à página de cadastro
        }

        autenticacaoService.salvarUsuario(usuario); // Método para salvar o usuário no banco
        return "redirect:/success"; // Redireciona para a página de sucesso
    }

    @GetMapping("/success")
    public String success() {
        return "success"; // Retorna a página de sucesso
    }
}
