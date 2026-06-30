package com.microservice.resenaycalificacion.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    
    private long idProducto;
    private String nombProducto;
    private double total;
}
