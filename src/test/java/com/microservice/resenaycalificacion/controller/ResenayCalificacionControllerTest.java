package com.microservice.resenaycalificacion.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.service.ResenayCalificacionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResenayCalificacionController.class)
class ResenayCalificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenayCalificacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ResenayCalificacion resena;

    @BeforeEach
    void setUp() {

        resena = new ResenayCalificacion();

        resena.setIdResena(1L);
        resena.setIdCliente(10L);
        resena.setIdProducto(20L);
        resena.setResena("Excelente producto");
        resena.setCalificacion(5);
        resena.setFecha(LocalDate.now());
    }

    @Test
    void crearResenaDebeRetornar201() throws Exception {

        when(service.generarResenaYCalificacion(resena))
                .thenReturn(resena);

        mockMvc.perform(
                post("/api/v1/resenas")
                        .with(user("admin"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resena)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.idResena").value(1))

                .andExpect(jsonPath("$._links.self").exists())

                .andExpect(jsonPath("$._links.resenas").exists());
    }

    @Test
    void listarResenasDebeRetornar200() throws Exception {

        when(service.listarResenasYCalificaciones())
                .thenReturn(List.of(resena));

        mockMvc.perform(
                get("/api/v1/resenas")
                        .with(user("admin")))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath(
                                "$._embedded.resenayCalificacionList[0].idResena")
                                .value(1))

                .andExpect(
                        jsonPath("$._links.self")
                                .exists());
    }

    @Test
    void buscarPorIdDebeRetornar200() throws Exception {

        when(service.buscarPorId(1L))
                .thenReturn(resena);

        mockMvc.perform(
                get("/api/v1/resenas/buscar/1")
                        .with(user("admin")))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.idResena").value(1))

                .andExpect(jsonPath("$._links.self").exists())

                .andExpect(jsonPath("$._links.resenas").exists());
    }

    @Test
    void actualizarResenaDebeRetornar200() throws Exception {

        ResenayCalificacion actualizada =
                new ResenayCalificacion();

        actualizada.setIdResena(1L);
        actualizada.setResena("Muy bueno");
        actualizada.setCalificacion(4);

        when(service.actualizarResenaYCalificacion(
                1L,
                actualizada))
                .thenReturn(actualizada);

        mockMvc.perform(
                put("/api/v1/resenas/actualizar/1")
                        .with(user("admin"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizada)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.resena")
                        .value("Muy bueno"))

                .andExpect(jsonPath("$.calificacion")
                        .value(4))

                .andExpect(jsonPath("$._links.self")
                        .exists())

                .andExpect(jsonPath("$._links.resenas")
                        .exists());
    }

    @Test
    void eliminarResenaDebeRetornar200() throws Exception {

        mockMvc.perform(
                delete("/api/v1/resenas/eliminar/1")
                        .with(user("admin"))
                        .with(csrf()))

                .andExpect(status().isOk())

                .andExpect(content().string(
                        "Reseña eliminada correctamente"));
    }
}