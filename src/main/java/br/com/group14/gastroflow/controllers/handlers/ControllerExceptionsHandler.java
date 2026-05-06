package br.com.group14.gastroflow.controllers.handlers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.group14.gastroflow.services.exceptions.AuthValidationException;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionsHandler.class);
    private static final String ERROR_BASE_URI = "/errors/";

    // ── Validação de negócio ────────────────────────────────────────────────

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(ValidationException e) {
        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "validation",
                "Validation Error",
                "One or more business rules were violated");
        problem.setProperty("errors", e.getErrors());
        return ResponseEntity.badRequest().body(problem);
    }

    // ── Bean Validation (@Valid) ────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();

        for (var error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "validation",
                "Validation Error",
                "One or more fields are invalid");
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }

    // ── Constraint Violations (e.g. from @Validated on service layer) ───────

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>();

        for (var error : e.getConstraintViolations()) {
            errors.add(error.getPropertyPath() + ": " + error.getMessage());
        }

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "validation",
                "Validation Error",
                "One or more constraints were violated");
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }

    // ── Recurso não encontrado ──────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException e) {
        ProblemDetail problem = buildProblem(
                HttpStatus.NOT_FOUND,
                "not-found",
                "Resource Not Found",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    // ── Conflito de integridade (unique constraints) ────────────────────────

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String detail = extractFriendlyMessage(e);
        ProblemDetail problem = buildProblem(
                HttpStatus.CONFLICT,
                "conflict",
                "Data Conflict",
                detail);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    // ── Uso inválido da API de acesso a dados ───────────────────────────────

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ProblemDetail> handleInvalidDataAccess(InvalidDataAccessApiUsageException e) {
        logger.error("InvalidDataAccessApiUsageException: {}", e.getMessage());
        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "invalid-request",
                "Invalid Request",
                e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    // ── Autenticação ────────────────────────────────────────────────────────

    @ExceptionHandler(AuthValidationException.class)
    public ResponseEntity<ProblemDetail> handleAuthValidation(AuthValidationException e) {
        ProblemDetail problem = buildProblem(
                HttpStatus.UNAUTHORIZED,
                "unauthorized",
                "Unauthorized",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    private ProblemDetail buildProblem(HttpStatus status, String errorType,
            String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create(ERROR_BASE_URI + errorType));
        problem.setTitle(title);
        return problem;
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