package com.esports.ms_torneos.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import com.esports.ms_torneos.dto.ApiResponse;
import com.esports.ms_torneos.dto.JuegoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JuegoClient {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8085/api/v1/juegos/";

    public JuegoResponse obtenerJuego(Long id, String token) {
        ApiResponse<JuegoResponse> response = webClient.get()
                .uri(baseUrl + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<JuegoResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}