package com.microservice.resenaycalificacion.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.resenaycalificacion.dto.ClienteDTO;
import com.microservice.resenaycalificacion.dto.ProductoDTO;
import com.microservice.resenaycalificacion.model.ResenayCalificacion;
import com.microservice.resenaycalificacion.repository.ResenayCalificacionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResenayCalificacionService {

    @Autowired
    private ResenayCalificacionRepository repository;

    @Autowired
    private RestTemplate restTemplate;
    

    private static final String URL_CLIENTES =
            "http://localhost:8081/api/v1/clientes/buscar/";

    private static final String URL_PRODUCTOS =
            "http://localhost:8086/api/v1/productos/buscar/";

    public ResenayCalificacion generarResenaYCalificacion(
            ResenayCalificacion resena) {

        ClienteDTO cliente =
                restTemplate.getForObject(
                        URL_CLIENTES + resena.getIdCliente(),
                        ClienteDTO.class);

        if (cliente == null) {
            throw new RuntimeException(
                    "Cliente no encontrado");
        }

        ProductoDTO producto =
                restTemplate.getForObject(
                        URL_PRODUCTOS + resena.getIdProducto(),
                        ProductoDTO.class);

        if (producto == null) {
            throw new RuntimeException(
                    "Producto no encontrado");
        }

        resena.setFecha(LocalDate.now());

        return repository.save(resena);
    }

    public List<ResenayCalificacion> listarResenasYCalificaciones() {

        return repository.findAll();
    }

    public List<ResenayCalificacion> listarPorProducto(
            Long idProducto) {

        return repository.findByIdProducto(idProducto);
    }

    public List<ResenayCalificacion> listarPorCliente(
            Long idCliente) {

        return repository.findByIdCliente(idCliente);
    }

    public ResenayCalificacion buscarPorId(Long idResena) {

        return repository.findById(idResena)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reseña no encontrada"));
    }

    public ResenayCalificacion actualizarResenaYCalificacion(
            Long idResena,
            ResenayCalificacion nuevaResena) {

        ResenayCalificacion resenaExistente =
                buscarPorId(idResena);

        resenaExistente.setResena(
                nuevaResena.getResena());

        resenaExistente.setCalificacion(
                nuevaResena.getCalificacion());

        return repository.save(resenaExistente);
    }

    public void eliminarResenaYCalificacion(Long idResena) {

        ResenayCalificacion resena =
                buscarPorId(idResena);

        repository.delete(resena);
    }
}


