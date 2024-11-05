package br.com.fiap.sprint4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/index")
    public String home() {
        return "index"; // Retorna a página inicial
    }

    @GetMapping("/publico")
    public String paginaPublica() {
        return "publico"; // Retorna uma página pública, se necessário
    }
}
