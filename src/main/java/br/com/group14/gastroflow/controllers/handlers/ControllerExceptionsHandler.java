package br.com.group14.gastroflow.controllers.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.group14.gastroflow.controllers.CustomerController;
import br.com.group14.gastroflow.dtos.exceptions.ResourceNotFoundDTO;
import br.com.group14.gastroflow.dtos.exceptions.ValidationErrorDTO;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;

@ControllerAdvice
public class ControllerExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationException(ValidationException e) {

        var status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(new ValidationErrorDTO(e.getErrors(), status.value()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDTO> handlerResourceNotFoundException(
            ResourceNotFoundException e) {

        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(
                status.value())
                .body(new ResourceNotFoundDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();

        for (var error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ValidationErrorDTO> handlerDataIntegrityViolationException(
            DataIntegrityViolationException e) {

        var status = HttpStatus.CONFLICT;
        String message = extractFriendlyMessage(e);

        return ResponseEntity
                .status(status)
                .body(new ValidationErrorDTO(List.of(message), status.value()));
    }

    private String extractFriendlyMessage(DataIntegrityViolationException e) {
        String msg = e.getMessage() != null ? e.getMessage() : "";

        if (msg.contains("customers_cpf_key")) {
            return "Provided CPF is already registered.";
        }

        if (msg.contains("users_email_key")) {
            return "Provided email is already registered.";
        }

        if (msg.contains("users_login_key")) {
            return "Provided login is already registered.";
        }

        logger.error("Unhandled DataIntegrityViolationException: {}", msg);
        return "Data integrity error.";
    }

}