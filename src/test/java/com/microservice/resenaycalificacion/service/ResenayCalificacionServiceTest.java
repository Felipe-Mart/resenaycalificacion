package com.microservice.resenaycalificacion.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.microservice.resenaycalificacion.dto.ClienteDTO;
import com.microservice.resenaycalificacion.dto.ProductoDTO;
import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.repository.ResenayCalificacionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ResenayCalificacionServiceTest {

    @Mock
    private ResenayCalificacionRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ResenayCalificacionService service;

    private ResenayCalificacion resena;
    private ClienteDTO cliente;
    private ProductoDTO producto;

    @BeforeEach
    void setUp() {

        resena = new ResenayCalificacion();

        resena.setIdResena(1L);
        resena.setIdCliente(10L);
        resena.setIdProducto(20L);
        resena.setResena("Excelente producto");
        resena.setCalificacion(5);

        cliente = new ClienteDTO();
        cliente.setIdCliente(10L);

        producto = new ProductoDTO();
        producto.setIdProducto(20L);
    }

    @Test
    void generarResenaYCalificacionDebeGuardarCorrectamente() {

        when(restTemplate.getForObject(
                "http://localhost:8081/api/v1/clientes/buscar/10",
                ClienteDTO.class))
                .thenReturn(cliente);

        when(restTemplate.getForObject(
                "http://localhost:8086/api/v1/productos/buscar/20",
                ProductoDTO.class))
                .thenReturn(producto);

        when(repository.save(any(ResenayCalificacion.class)))
                .thenReturn(resena);

        ResenayCalificacion resultado =
                service.generarResenaYCalificacion(resena);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());

        verify(repository).save(resena);
    }

    @Test
    void generarResenaDebeLanzarExcepcionSiClienteNoExiste() {

        when(restTemplate.getForObject(
                anyString(),
                eq(ClienteDTO.class)))
                .thenReturn(null);

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.generarResenaYCalificacion(resena));

        assertEquals(
                "Cliente no encontrado",
                ex.getMessage());
    }

    @Test
    void generarResenaDebeLanzarExcepcionSiProductoNoExiste() {

        when(restTemplate.getForObject(
                anyString(),
                eq(ClienteDTO.class)))
                .thenReturn(cliente);

        when(restTemplate.getForObject(
                anyString(),
                eq(ProductoDTO.class)))
                .thenReturn(null);

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.generarResenaYCalificacion(resena));

        assertEquals(
                "Producto no encontrado",
                ex.getMessage());
    }

    @Test
    void listarResenasDebeRetornarLista() {

        when(repository.findAll())
                .thenReturn(List.of(resena));

        List<ResenayCalificacion> resultado =
                service.listarResenasYCalificaciones();

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPorProductoDebeRetornarLista() {

        when(repository.findByIdProducto(20L))
                .thenReturn(List.of(resena));

        List<ResenayCalificacion> resultado =
                service.listarPorProducto(20L);

        assertEquals(1, resultado.size());
        assertEquals(20L,
                resultado.get(0).getIdProducto());
    }

    @Test
    void listarPorClienteDebeRetornarLista() {

        when(repository.findByIdCliente(10L))
                .thenReturn(List.of(resena));

        List<ResenayCalificacion> resultado =
                service.listarPorCliente(10L);

        assertEquals(1, resultado.size());
        assertEquals(10L,
                resultado.get(0).getIdCliente());
    }

    @Test
    void buscarPorIdDebeRetornarResena() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        ResenayCalificacion resultado =
                service.buscarPorId(1L);

        assertEquals(1L, resultado.getIdResena());
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionSiNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.buscarPorId(1L));

        assertEquals(
                "Reseña no encontrada",
                ex.getMessage());
    }

    @Test
    void actualizarResenaDebeActualizarCorrectamente() {

        ResenayCalificacion nueva =
                new ResenayCalificacion();

        nueva.setResena("Producto actualizado");
        nueva.setCalificacion(4);

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        when(repository.save(any()))
                .thenReturn(resena);

        ResenayCalificacion resultado =
                service.actualizarResenaYCalificacion(
                        1L,
                        nueva);

        assertEquals(
                "Producto actualizado",
                resultado.getResena());

        assertEquals(
                4,
                resultado.getCalificacion());
    }

    @Test
    void eliminarResenaDebeEliminarCorrectamente() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        doNothing()
                .when(repository)
                .delete(resena);

        service.eliminarResenaYCalificacion(1L);

        verify(repository)
                .delete(resena);
    }
}
