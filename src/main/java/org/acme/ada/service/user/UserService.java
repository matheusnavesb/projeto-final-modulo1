package org.acme.ada.service.user;

import org.acme.ada.dto.user.UserDTO;
import org.acme.ada.dto.user.UserResponseDTO;
import org.acme.ada.model.User;

public interface UserService {

    UserResponseDTO create(UserDTO dto);

    UserResponseDTO getLoggedUser(String email);

    User findEntityByEmail(String email);
}
