package org.acme.ada.service.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.ada.dto.user.LoginDTO;
import org.acme.ada.dto.user.TokenResponseDTO;
import org.acme.ada.exception.NotFoundException;
import org.acme.ada.model.User;
import org.acme.ada.security.JwtUtil;
import org.acme.ada.service.user.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AuthServiceImpl implements AuthService{

    @Inject
    UserService userService;

    @Inject
    JwtUtil jwtUtil;

    @ConfigProperty(name = "jwt.expiration.seconds", defaultValue = "3600")
    long expirationInSeconds;

    @Override
    public TokenResponseDTO generateToken(LoginDTO dto) {
        // busca usuário
        User user = userService.findEntityByEmail(dto.email());

        // valida senha
        if (!user.getPassword().equals(dto.password())) {
            throw new NotFoundException("Invalid credentials");
        }

        // gera token
        String token = jwtUtil.generateToken(user, expirationInSeconds);

        return new TokenResponseDTO(token, expirationInSeconds);
    }
}
