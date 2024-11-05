package br.com.fiap.sprint4.service;

import br.com.fiap.sprint4.model.Usuario;
import br.com.fiap.sprint4.repository.UsuarioRepository;
import br.com.fiap.sprint4.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AutenticacaoService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder; // Injeção do encoder
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usando findByEmail que retorna um Optional<Usuario>
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Retorna um UsuarioDetails com o usuário encontrado
        return new UsuarioDetails(usuario);
    }

    public void salvarUsuario(Usuario usuario) {
        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario); // Salva o usuário no banco de dados
    }

    public boolean autenticar(String email, String senha) {
        // Busca o usuário pelo email
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email); // Retorna um Optional<Usuario>

        // Verifica se o usuário existe e se a senha bate com a senha codificada
        return optionalUsuario
                .map(usuario -> passwordEncoder.matches(senha, usuario.getSenha()))
                .orElse(false); // Retorna falso se o usuário não existir
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email); // Retorna um Optional de Usuario
    }
}
