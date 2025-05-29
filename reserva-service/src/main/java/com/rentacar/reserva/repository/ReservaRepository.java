package com.rentacar.reserva.repository;

import com.rentacar.reserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    List<Reserva> findByClienteId(Long clienteId);
    
    List<Reserva> findByVehiculoId(Long vehiculoId);
    
    @Query("SELECT r FROM Reserva r WHERE r.vehiculoId = :vehiculoId " +
           "AND r.estado NOT IN ('CANCELADA', 'COMPLETADA') " +
           "AND ((r.fechaInicio BETWEEN :fechaInicio AND :fechaFin) " +
           "OR (r.fechaFin BETWEEN :fechaInicio AND :fechaFin) " +
           "OR (:fechaInicio BETWEEN r.fechaInicio AND r.fechaFin))")
    List<Reserva> findReservasSuperpuestas(
            @Param("vehiculoId") Long vehiculoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
} 