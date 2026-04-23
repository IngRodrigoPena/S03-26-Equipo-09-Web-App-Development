package com.startupcrm.crm_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private Long contactoId;
    private String asunto;
    private String contenido;
}

