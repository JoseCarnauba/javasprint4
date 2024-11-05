package br.com.fiap.sprint4.config;

import br.com.fiap.sprint4.model.Usuario;
import br.com.fiap.sprint4.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tenha cuidado ao desabilitar o CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user/login", "/user/cadastro", "/success", "/resources/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .permitAll()
                        .defaultSuccessUrl("/home/index", true) // Redireciona para a página inicial após login
                        .failureUrl("/user/login?erro=true") // Redireciona para a página de login em caso de erro
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/user/login?logout=true") // Redireciona para a página de login após logout
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(username -> {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
            if (usuario.isEmpty()) {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
            Collection authorities = new ArrayList<>(); // Aqui você pode adicionar as autoridades do usuário, se necessário

            return new org.springframework.security.core.userdetails.User(
                    usuario.get().getEmail(),
                    usuario.get().getSenha(),
                    authorities
            );
        }).passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
