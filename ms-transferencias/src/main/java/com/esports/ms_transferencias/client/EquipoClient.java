package com.esports.ms_transferencias.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import com.esports.ms_transferencias.dto.ApiResponse;
import com.esports.ms_transferencias.dto.EquipoResponse;

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
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<EquipoResponse>>() {
                })
                .block();

        return response != null ? response.getData() : null;
    }
}