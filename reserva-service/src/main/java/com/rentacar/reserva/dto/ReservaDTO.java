package com.rentacar.reserva.dto;

import com.rentacar.reserva.model.Reserva;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservaDTO {
    private Long id;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El ID del vehículo es obligatorio")
    private Long vehiculoId;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private LocalDate fechaInicio;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser futura")
    private LocalDate fechaFin;
    
    private BigDecimal precioTotal;
    private Reserva.EstadoReserva estado;
    private LocalDate fechaCreacion;
    
    // Campos adicionales para mostrar información del vehículo
    private String marcaVehiculo;
    private String modeloVehiculo;
    private String placaVehiculo;
} 