package br.com.controlefinanceiro.ms.centrocusto.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}