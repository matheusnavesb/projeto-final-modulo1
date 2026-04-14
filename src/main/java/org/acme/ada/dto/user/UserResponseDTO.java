package org.acme.ada.dto.user;

import org.acme.ada.model.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role
) {
    public static UserResponseDTO valueOf(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
