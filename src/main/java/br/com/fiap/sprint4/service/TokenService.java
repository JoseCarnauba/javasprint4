package br.com.fiap.sprint4.service;

import br.com.fiap.sprint4.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}") // Use uma propriedade externa para a chave secreta
    private String secret;

    private final long expiration = 86400000; // 1 dia em milissegundos

    /**
     * Gera um token JWT com base em um objeto Usuario.
     *
     * @param usuario O usuário para o qual o token deve ser gerado.
     * @return O token JWT gerado.
     */
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
