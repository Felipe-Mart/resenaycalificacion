package com.microservice.resenaycalificacion.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.service.ResenayCalificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/resenas")
@Validated
@Tag(name = "Reseñas y Calificaciones", description = "Operaciones para la creación, consulta, actualización y eliminación de reseñas y calificaciones.")
public class ResenayCalificacionController {

        @Autowired
        private ResenayCalificacionService service;

        @PostMapping
        @Operation(summary = "Crear reseña y calificación", description = "Registra una nueva reseña y calificación en el sistema, devolviendo el recurso creado junto con sus enlaces HATEOAS.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Reseña y calificación creadas correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos de la reseña inválidos o incompletos")
        })
        public ResponseEntity<EntityModel<ResenayCalificacion>> crearResenaYCalificacion(
                        @Valid @RequestBody ResenayCalificacion resena) {

                ResenayCalificacion nueva = service.generarResenaYCalificacion(resena);

                EntityModel<ResenayCalificacion> recurso = EntityModel.of(nueva);

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
        @Operation(summary = "Listar reseñas", description = "Obtiene una lista de todas las reseñas y calificaciones registradas en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida correctamente")
        })
        public CollectionModel<EntityModel<ResenayCalificacion>> listarResenasYCalificaciones() {

                List<EntityModel<ResenayCalificacion>> resenas = service.listarResenasYCalificaciones()
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
        @Operation(summary = "Buscar reseña por ID", description = "Obtiene los detalles de una reseña específica mediante su identificador único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
        })
        public ResponseEntity<EntityModel<ResenayCalificacion>> buscarPorId(@PathVariable Long id) {

                ResenayCalificacion resena = service.buscarPorId(id);

                EntityModel<ResenayCalificacion> recurso = EntityModel.of(resena);

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
        @Operation(summary = "Actualizar reseña y calificación", description = "Modifica los datos de una reseña y calificación existente en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos enviados inválidos para la actualización"),
                        @ApiResponse(responseCode = "404", description = "Reseña no encontrada para actualizar")
        })
        public ResponseEntity<EntityModel<ResenayCalificacion>> actualizarResenaYCalificacion(
                        @PathVariable Long id,
                        @Valid @RequestBody ResenayCalificacion resenaActualizada) {

                ResenayCalificacion actualizada = service.actualizarResenaYCalificacion(
                                id,
                                resenaActualizada);

                EntityModel<ResenayCalificacion> recurso = EntityModel.of(actualizada);

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
        @Operation(summary = "Eliminar reseña", description = "Elimina de forma permanente una reseña y calificación del sistema mediante su identificador.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reseña eliminada correctamente"),
                        @ApiResponse(responseCode = "404", description = "Reseña no encontrada para eliminar")
        })
        public ResponseEntity<String> eliminarResenaYCalificacion(
                        @PathVariable Long id) {

                service.eliminarResenaYCalificacion(id);

                return ResponseEntity.ok(
                                "Reseña eliminada correctamente");
        }
}