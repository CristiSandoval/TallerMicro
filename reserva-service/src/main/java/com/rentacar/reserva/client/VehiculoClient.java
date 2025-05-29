package com.rentacar.reserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vehiculo-service", url = "${services.vehiculo-service.url}")
public interface VehiculoClient {
    
    @GetMapping("/api/vehiculos/{id}")
    ResponseEntity<VehiculoDTO> obtenerVehiculo(@PathVariable("id") Long id);
    
    @PatchMapping("/api/vehiculos/{id}/estado")
    ResponseEntity<VehiculoDTO> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("estado") String estado);
} 