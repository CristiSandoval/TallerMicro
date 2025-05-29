package com.rentacar.vehiculo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "vehiculos")
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String marca;
    
    @Column(nullable = false)
    private String modelo;
    
    @Column(nullable = false, unique = true)
    private String placa;
    
    @Column(nullable = false)
    private Integer anio;
    
    @Column(nullable = false)
    private BigDecimal tarifaDiaria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
    
    public enum EstadoVehiculo {
        DISPONIBLE,
        ALQUILADO,
        MANTENIMIENTO
    }
} 