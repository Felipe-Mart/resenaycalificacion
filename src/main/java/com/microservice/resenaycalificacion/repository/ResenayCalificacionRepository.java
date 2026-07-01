package com.microservice.resenaycalificacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.resenaycalificacion.model.ResenayCalificacion;

@Repository

public interface ResenayCalificacionRepository extends JpaRepository<ResenayCalificacion,Long>{ 
    List<ResenayCalificacion> findByIdProducto(Long idProducto);

    List<ResenayCalificacion> findByIdCliente(Long idCliente);
}
