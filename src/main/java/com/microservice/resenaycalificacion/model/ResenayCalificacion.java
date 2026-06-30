package com.microservice.resenaycalificacion.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reseñasycalificaciones")
public class ReseñayCalificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    // Referencias a otros microservicios
    @NotNull(message = "El id del cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;

    @NotNull(message = "El id del producto es obligatorio")
    @Column(nullable = false)
    private Long idProducto;

    @NotBlank(message = "La reseña es obligatoria")
    @Size(
        min = 5,
        max = 500,
        message = "La reseña debe tener entre 5 y 500 caracteres"
    )
    @Column(nullable = false, length = 500)
    private String resena;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private Integer calificacion;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;
}
