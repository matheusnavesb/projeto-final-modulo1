package org.acme.ada.dto.user;

public record TokenResponseDTO(
        String token,
        long expiresIn
) {
}
