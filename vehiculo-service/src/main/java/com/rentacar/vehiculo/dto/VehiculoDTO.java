package com.rentacar.vehiculo.dto;

import com.rentacar.vehiculo.model.Vehiculo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class VehiculoDTO {
    private Long id;
    
    @NotBlank(message = "La marca es obligatoria")
    private String marca;
    
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    
    @NotBlank(message = "La placa es obligatoria")
    private String placa;
    
    @NotNull(message = "El año es obligatorio")
    @Positive(message = "El año debe ser un número positivo")
    private Integer anio;
    
    @NotNull(message = "La tarifa diaria es obligatoria")
    @Positive(message = "La tarifa diaria debe ser un valor positivo")
    private BigDecimal tarifaDiaria;
    
    private Vehiculo.EstadoVehiculo estado;
} 