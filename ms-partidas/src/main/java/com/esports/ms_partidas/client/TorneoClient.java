package com.esports.ms_partidas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.esports.ms_partidas.dto.ApiResponse;
import com.esports.ms_partidas.dto.TorneoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TorneoClient {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8086/api/v1/torneos/";

    public TorneoResponse obtenerTorneo(Long id, String token) {
        ApiResponse<TorneoResponse> response = webClient.get()
                .uri(baseUrl + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<TorneoResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}