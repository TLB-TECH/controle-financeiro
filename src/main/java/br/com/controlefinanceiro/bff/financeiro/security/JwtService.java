package br.com.controlefinanceiro.bff.financeiro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey chave;

    @PostConstruct
    public void init() {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String getEmailDoToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();}
}