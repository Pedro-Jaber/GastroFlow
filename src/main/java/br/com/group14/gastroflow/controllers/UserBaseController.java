package br.com.group14.gastroflow.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.services.UserService;

@RestController
@RequestMapping("/users")
public class UserBaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserBaseController.class);

    private final UserService userService;

    public UserBaseController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserBase>> getFindAllUsers(Pageable pageable) {
        logger.info("GET => /users - Request: {}", pageable);
        Page<UserBase> users = userService.findAll(pageable);
        // logger.info("Response: {}", users);
        return ResponseEntity.ok(users);
    }

}
