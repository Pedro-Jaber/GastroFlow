package br.com.group14.gastroflow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.group14.gastroflow.dtos.LoginRequestDTO;
import br.com.group14.gastroflow.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> postLogin(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        // logger.info("POST => /auth/login - Body: {}", loginRequestDTO);
        logger.info("POST => /auth/login");
        authService.login(loginRequestDTO.login(), loginRequestDTO.password());
        return ResponseEntity.ok().build();
    }
}
