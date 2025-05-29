package com.rentacar.vehiculo.service;

import com.rentacar.vehiculo.dto.VehiculoDTO;
import com.rentacar.vehiculo.model.Vehiculo;
import com.rentacar.vehiculo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<VehiculoDTO> obtenerTodos() {
        return vehiculoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehiculoDTO obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con ID: " + id));
    }

    @Transactional
    public VehiculoDTO crear(VehiculoDTO vehiculoDTO) {
        if (vehiculoRepository.existsByPlaca(vehiculoDTO.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa: " + vehiculoDTO.getPlaca());
        }
        
        Vehiculo vehiculo = convertirAEntidad(vehiculoDTO);
        vehiculo.setEstado(Vehiculo.EstadoVehiculo.DISPONIBLE);
        return convertirADTO(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public VehiculoDTO actualizar(Long id, VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con ID: " + id));
        
        actualizarEntidad(vehiculo, vehiculoDTO);
        return convertirADTO(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new EntityNotFoundException("Vehículo no encontrado con ID: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    @Transactional
    public VehiculoDTO actualizarEstado(Long id, Vehiculo.EstadoVehiculo nuevoEstado) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con ID: " + id));
        
        vehiculo.setEstado(nuevoEstado);
        return convertirADTO(vehiculoRepository.save(vehiculo));
    }

    private VehiculoDTO convertirADTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setAnio(vehiculo.getAnio());
        dto.setTarifaDiaria(vehiculo.getTarifaDiaria());
        dto.setEstado(vehiculo.getEstado());
        return dto;
    }

    private Vehiculo convertirAEntidad(VehiculoDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setTarifaDiaria(dto.getTarifaDiaria());
        vehiculo.setEstado(dto.getEstado() != null ? dto.getEstado() : Vehiculo.EstadoVehiculo.DISPONIBLE);
        return vehiculo;
    }

    private void actualizarEntidad(Vehiculo vehiculo, VehiculoDTO dto) {
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setTarifaDiaria(dto.getTarifaDiaria());
        if (dto.getEstado() != null) {
            vehiculo.setEstado(dto.getEstado());
        }
    }
} 