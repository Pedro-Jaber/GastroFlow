package br.com.group14.gastroflow.dtos.update;

import jakarta.validation.constraints.NotBlank;

public record PasswordUpdateDTO(
                @NotBlank(message = "Current password is required") String currentPassword,

                @NotBlank(message = "New password is required") String newPassword,

                @NotBlank(message = "Confirm password is required") String confirmPassword) {
}
