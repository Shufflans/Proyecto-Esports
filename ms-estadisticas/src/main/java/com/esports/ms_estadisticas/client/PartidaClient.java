package com.esports.ms_estadisticas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.esports.ms_estadisticas.dto.ApiResponse;
import com.esports.ms_estadisticas.dto.PartidaResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PartidaClient {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8087/api/v1/partidas/";

    public PartidaResponse obtenerPartida(Long id, String token) {
        ApiResponse<PartidaResponse> response = webClient.get()
                .uri(baseUrl + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<PartidaResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}