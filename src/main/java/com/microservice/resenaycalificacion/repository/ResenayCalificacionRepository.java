package com.microservice.resenaycalificacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.resenaycalificacion.model.ReseñayCalificacion;

@Repository

public interface ReseñayCalificacionRepository extends JpaRepository<ReseñayCalificacion,Long>{ 
    List<ReseñayCalificacion> findByIdProducto(Long idProducto);

    List<ReseñayCalificacion> findByIdCliente(Long idCliente);
}
