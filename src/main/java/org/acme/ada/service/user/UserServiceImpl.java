package org.acme.ada.service.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.dto.user.UserDTO;
import org.acme.ada.dto.user.UserResponseDTO;
import org.acme.ada.exception.ConflictException;
import org.acme.ada.exception.NotFoundException;
import org.acme.ada.model.User;
import org.acme.ada.repository.UserRepository;

@ApplicationScoped
public class UserServiceImpl implements UserService{

    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO create(UserDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password()); // depois podemos trocar por hash
        user.setRole("USER");

        userRepository.persist(user);

        return UserResponseDTO.valueOf(user);
    }

    @Override
    public UserResponseDTO getLoggedUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return UserResponseDTO.valueOf(user);
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}