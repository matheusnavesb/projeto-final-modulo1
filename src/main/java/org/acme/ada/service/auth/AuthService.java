package org.acme.ada.service.auth;

import org.acme.ada.dto.user.LoginDTO;
import org.acme.ada.dto.user.TokenResponseDTO;

public interface AuthService {

    TokenResponseDTO generateToken(LoginDTO dto);
}
