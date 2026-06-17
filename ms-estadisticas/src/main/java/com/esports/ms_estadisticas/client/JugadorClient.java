package com.esports.ms_estadisticas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.esports.ms_estadisticas.dto.ApiResponse;
import com.esports.ms_estadisticas.dto.JugadorResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JugadorClient {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8084/api/v1/jugadores/";

    public JugadorResponse obtenerJugador(Long id, String token) {
        ApiResponse<JugadorResponse> response = webClient.get()
                .uri(baseUrl + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<JugadorResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}