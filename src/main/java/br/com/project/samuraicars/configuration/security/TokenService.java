package br.com.project.samuraicars.configuration.security;

import br.com.project.samuraicars.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final String secret = "SamuraiCars";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Samurai's cars")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            throw new RuntimeException("Error generating JWT token");
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Samurai's cars")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            exception.printStackTrace();
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(24 * 7).toInstant(ZoneOffset.UTC);
    }
}
