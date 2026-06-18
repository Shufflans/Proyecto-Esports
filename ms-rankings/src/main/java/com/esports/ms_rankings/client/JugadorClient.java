package com.esports.ms_rankings.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import com.esports.ms_rankings.dto.ApiResponse;
import com.esports.ms_rankings.dto.JugadorResponse;

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
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<JugadorResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}