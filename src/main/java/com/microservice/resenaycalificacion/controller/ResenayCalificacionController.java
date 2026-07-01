package com.microservice.resenaycalificacion.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    public ResponseEntity<EntityModel<ResenayCalificacion>>
    crearResenaYCalificacion(
            @Valid @RequestBody ResenayCalificacion resena) {

        ResenayCalificacion nueva =
                service.generarResenaYCalificacion(resena);

        EntityModel<ResenayCalificacion> recurso =
                EntityModel.of(nueva);

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .buscarPorId(nueva.getIdResena()))
                .withSelfRel());

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .listarResenasYCalificaciones())
                .withRel("resenas"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recurso);
    }

    @GetMapping
    public CollectionModel<EntityModel<ResenayCalificacion>>
    listarResenasYCalificaciones() {

        List<EntityModel<ResenayCalificacion>> resenas =
                service.listarResenasYCalificaciones()
                        .stream()
                        .map(r -> EntityModel.of(
                                r,
                                linkTo(methodOn(
                                        ResenayCalificacionController.class)
                                        .buscarPorId(r.getIdResena()))
                                        .withSelfRel()))
                        .collect(Collectors.toList());

        return CollectionModel.of(
                resenas,
                linkTo(methodOn(
                        ResenayCalificacionController.class)
                        .listarResenasYCalificaciones())
                        .withSelfRel());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<EntityModel<ResenayCalificacion>>
    buscarPorId(@PathVariable Long id) {

        ResenayCalificacion resena =
                service.buscarPorId(id);

        EntityModel<ResenayCalificacion> recurso =
                EntityModel.of(resena);

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .buscarPorId(id))
                .withSelfRel());

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .listarResenasYCalificaciones())
                .withRel("resenas"));

        return ResponseEntity.ok(recurso);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<ResenayCalificacion>>
    actualizarResenaYCalificacion(
            @PathVariable Long id,
            @Valid @RequestBody ResenayCalificacion resenaActualizada) {

        ResenayCalificacion actualizada =
                service.actualizarResenaYCalificacion(
                        id,
                        resenaActualizada);

        EntityModel<ResenayCalificacion> recurso =
                EntityModel.of(actualizada);

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .buscarPorId(id))
                .withSelfRel());

        recurso.add(linkTo(
                methodOn(ResenayCalificacionController.class)
                        .listarResenasYCalificaciones())
                .withRel("resenas"));

        return ResponseEntity.ok(recurso);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String>
    eliminarResenaYCalificacion(
            @PathVariable Long id) {

        service.eliminarResenaYCalificacion(id);

        return ResponseEntity.ok(
                "Reseña eliminada correctamente");
    }
}