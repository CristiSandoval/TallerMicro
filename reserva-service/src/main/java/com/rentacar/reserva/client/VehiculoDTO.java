package com.rentacar.reserva.client;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehiculoDTO {
    private Long id;
    private String marca;
    private String modelo;
    private String placa;
    private Integer anio;
    private BigDecimal tarifaDiaria;
    private String estado;
} 