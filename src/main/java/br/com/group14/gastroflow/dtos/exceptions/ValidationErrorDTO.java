package br.com.group14.gastroflow.dtos.exceptions;

import java.util.List;

public record ValidationErrorDTO(
                List<String> errors,
                int status) {

}
