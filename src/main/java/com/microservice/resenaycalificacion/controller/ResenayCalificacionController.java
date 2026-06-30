package com.microservice.resenaycalificacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.service.ResenayCalificacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/resenas")
@Validated
public class ResenayCalificacionController {

    @Autowired
    private ResenayCalificacionService service;

    @PostMapping
    public ResponseEntity<?> crearResenaYCalificacion(
            @Valid @RequestBody ResenayCalificacion resena) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.generarResenaYCalificacion(resena));
    }

    @GetMapping
    public ResponseEntity<?> listarResenasYCalificaciones() {

        return ResponseEntity.ok(
                service.listarResenasYCalificaciones());
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<?> actualizarResenaYCalificacion(
            @PathVariable Long id,
            @Valid @RequestBody ResenayCalificacion resenaActualizada) {

        return ResponseEntity.ok(
                service.actualizarResenaYCalificacion(
                        id,
                        resenaActualizada));
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarResenaYCalificacion(
            @PathVariable Long id) {

        service.eliminarResenaYCalificacion(id);

        return ResponseEntity.ok(
                "Reseña eliminada correctamente");
    }
}
