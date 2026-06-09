package com.esports.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 24, message = "El nombre de usuario no puede ser tan largo")
    @Size(min = 4, message = "El nombre de usuario no puede ser tan corto")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "la contraseña debe tener más de 8 carácteres")
    private String password;
}
