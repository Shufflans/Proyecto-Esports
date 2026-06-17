package com.esports.ms_partidas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.esports.ms_partidas.dto.ApiResponse;
import com.esports.ms_partidas.dto.EquipoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EquipoClient {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8083/api/v1/equipos/";

    public EquipoResponse obtenerEquipo(Long id, String token) {
        ApiResponse<EquipoResponse> response = webClient.get()
                .uri(baseUrl + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<EquipoResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}