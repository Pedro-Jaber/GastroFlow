package br.com.group14.gastroflow.services.exceptions;

public class AuthValidationException extends RuntimeException {
    public AuthValidationException(String message) {
        super(message);
    }
}
