package com.rentacar.vehiculo.repository;

import com.rentacar.vehiculo.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByEstado(Vehiculo.EstadoVehiculo estado);
    boolean existsByPlaca(String placa);
} 