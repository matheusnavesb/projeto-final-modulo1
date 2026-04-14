package org.acme.ada.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.ada.model.User;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtUtil {

    public String generateToken(User user, long expirationInSeconds) {
        return Jwt.issuer("course-api")
                .upn(user.getEmail())
                .subject(user.getEmail())
                .groups(Set.of(user.getRole()))
                .expiresIn(Duration.ofSeconds(expirationInSeconds))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .sign();
    }
}
