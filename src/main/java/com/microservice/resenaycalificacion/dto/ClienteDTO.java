package com.microservice.resenaycalificacion.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long idCliente;
    private String nombre;
    private String rut;
    private String email;
}
