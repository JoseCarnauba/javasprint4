package br.com.fiap.sprint4.service;

import br.com.fiap.sprint4.model.Perfil;
import br.com.fiap.sprint4.model.Usuario;
import br.com.fiap.sprint4.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void salvar(Usuario usuario) {
        validarUsuario(usuario); // Chama a validação antes de salvar
        usuarioRepository.save(usuario); // Salva o usuário no banco de dados
    }

    private void validarUsuario(Usuario usuario) {
        // Verifica se o perfil é nulo
        if (usuario.getPerfis() == null || usuario.getPerfis().isEmpty()) {
            throw new IllegalArgumentException("Perfil não pode ser nulo ou vazio");
        }

        // Checa se algum perfil é um valor válido do enum
        boolean perfilValido = usuario.getPerfis().stream()
                .anyMatch(p -> p != null && Arrays.asList(Perfil.values()).contains(p));

        if (!perfilValido) {
            throw new IllegalArgumentException("Perfil inválido");
        }
    }
}
