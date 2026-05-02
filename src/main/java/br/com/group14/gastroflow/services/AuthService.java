package br.com.group14.gastroflow.services;

import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.repositories.UserBaseRepository;
import br.com.group14.gastroflow.services.exceptions.AuthValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserBaseRepository<UserBase<?>> userBaseRepository;

    public void login(String login, String password) {

        UserBase<?> user = userBaseRepository.findByLogin(login);

        if (user == null) {
            throw new AuthValidationException("Login and/or password are incorrect");
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthValidationException("Login and/or password are incorrect");
        }

        // return JWT

    }

}
