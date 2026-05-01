package br.com.group14.gastroflow.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserBase> findAll(Pageable pageable) {
        var usersPage = userRepository.findAll(pageable);
        // Page<UserResponseDTO> usersDTOPage = usersPage.map((UserBase user) -> new
        // UserResponseDTO(user));

        return usersPage;
    }
}
