package com.esports.ms_equipos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorResponse {
    private Long id;
    private String nickname;
    private String nombreReal;
    private Boolean activo;
}
