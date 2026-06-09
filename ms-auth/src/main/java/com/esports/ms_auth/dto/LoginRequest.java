package com.esports.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Debe ingresar un nombre de usuario.")
    private String username;

    @NotBlank(message = "Debe ingresar una contraseña")
    @Size(min = 8, max = 24, message = "La contraseña debe ser mayor a 8 carácteres.")
    private String password;
}
