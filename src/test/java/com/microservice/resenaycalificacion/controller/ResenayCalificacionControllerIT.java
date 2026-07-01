package com.microservice.resenaycalificacion.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;

import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.repository.ResenayCalificacionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ResenayCalificacionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResenayCalificacionRepository repository;

    private ResenayCalificacion resena;

    @BeforeEach
    void setUp() {

        repository.deleteAll();

        resena = new ResenayCalificacion();

        resena.setIdCliente(10L);
        resena.setIdProducto(20L);
        resena.setResena("Excelente producto");
        resena.setCalificacion(5);
        resena.setFecha(LocalDate.now());

        resena = repository.save(resena);
    }

    @Test
    void buscarPorIdDebeRetornar200() throws Exception {

        mockMvc.perform(
                get("/api/v1/resenas/buscar/"
                        + resena.getIdResena())
                        .with(user("admin")))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.idResena")
                                .value(resena.getIdResena()))

                .andExpect(
                        jsonPath("$._links.self")
                                .exists())

                .andExpect(
                        jsonPath("$._links.resenas")
                                .exists());
    }

    @Test
    void listarResenasDebeRetornar200() throws Exception {

        mockMvc.perform(
                get("/api/v1/resenas")
                        .with(user("admin")))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath(
                                "$._embedded.resenayCalificacionList[0].idResena")
                                .value(resena.getIdResena()))

                .andExpect(
                        jsonPath("$._links.self")
                                .exists());
    }

    @Test
    void eliminarResenaDebeEliminarCorrectamente()
            throws Exception {

        mockMvc.perform(
                delete("/api/v1/resenas/eliminar/"
                        + resena.getIdResena())
                        .with(user("admin"))
                        .with(csrf()))

                .andExpect(status().isOk())

                .andExpect(content().string(
                        "Reseña eliminada correctamente"));

        Optional<ResenayCalificacion> resultado =
                repository.findById(
                        resena.getIdResena());

        assert(resultado.isEmpty());
    }
}