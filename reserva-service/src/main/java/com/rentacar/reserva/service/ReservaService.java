package com.rentacar.reserva.service;

import com.rentacar.reserva.client.VehiculoClient;
import com.rentacar.reserva.client.VehiculoDTO;
import com.rentacar.reserva.dto.ReservaDTO;
import com.rentacar.reserva.model.Reserva;
import com.rentacar.reserva.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VehiculoClient vehiculoClient;

    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerTodas() {
        return reservaRepository.findAll().stream()
                .map(this::enriquecerReservaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaDTO obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .map(this::enriquecerReservaDTO)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Transactional
    public ReservaDTO crear(ReservaDTO reservaDTO) {
        validarDisponibilidad(reservaDTO);
        
        VehiculoDTO vehiculo = obtenerVehiculo(reservaDTO.getVehiculoId());
        reservaDTO.setPrecioTotal(calcularPrecioTotal(vehiculo.getTarifaDiaria(), 
                reservaDTO.getFechaInicio(), reservaDTO.getFechaFin()));
        
        Reserva reserva = convertirAEntidad(reservaDTO);
        reserva = reservaRepository.save(reserva);
        
        actualizarEstadoVehiculo(reserva.getVehiculoId(), "ALQUILADO");
        
        return enriquecerReservaDTO(reserva);
    }

    @Transactional
    public ReservaDTO actualizarEstado(Long id, Reserva.EstadoReserva nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        
        if (nuevoEstado == Reserva.EstadoReserva.CANCELADA || 
            nuevoEstado == Reserva.EstadoReserva.COMPLETADA) {
            actualizarEstadoVehiculo(reserva.getVehiculoId(), "DISPONIBLE");
        }
        
        reserva.setEstado(nuevoEstado);
        return enriquecerReservaDTO(reservaRepository.save(reserva));
    }

    private void validarDisponibilidad(ReservaDTO reservaDTO) {
        if (reservaDTO.getFechaInicio().isAfter(reservaDTO.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }

        List<Reserva> reservasSuperpuestas = reservaRepository.findReservasSuperpuestas(
                reservaDTO.getVehiculoId(),
                reservaDTO.getFechaInicio(),
                reservaDTO.getFechaFin());

        if (!reservasSuperpuestas.isEmpty()) {
            throw new IllegalStateException("El vehículo no está disponible en las fechas seleccionadas");
        }
    }

    private VehiculoDTO obtenerVehiculo(Long vehiculoId) {
        return vehiculoClient.obtenerVehiculo(vehiculoId)
                .getBody();
    }

    private void actualizarEstadoVehiculo(Long vehiculoId, String estado) {
        vehiculoClient.actualizarEstado(vehiculoId, estado);
    }

    private BigDecimal calcularPrecioTotal(BigDecimal tarifaDiaria, LocalDate fechaInicio, LocalDate fechaFin) {
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return tarifaDiaria.multiply(BigDecimal.valueOf(dias));
    }

    private ReservaDTO enriquecerReservaDTO(Reserva reserva) {
        ReservaDTO dto = convertirADTO(reserva);
        try {
            VehiculoDTO vehiculo = obtenerVehiculo(reserva.getVehiculoId());
            dto.setMarcaVehiculo(vehiculo.getMarca());
            dto.setModeloVehiculo(vehiculo.getModelo());
            dto.setPlacaVehiculo(vehiculo.getPlaca());
        } catch (Exception e) {
            // Si no se puede obtener la información del vehículo, continuamos sin ella
        }
        return dto;
    }

    private ReservaDTO convertirADTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setClienteId(reserva.getClienteId());
        dto.setVehiculoId(reserva.getVehiculoId());
        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setEstado(reserva.getEstado());
        dto.setFechaCreacion(reserva.getFechaCreacion());
        return dto;
    }

    private Reserva convertirAEntidad(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setClienteId(dto.getClienteId());
        reserva.setVehiculoId(dto.getVehiculoId());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setPrecioTotal(dto.getPrecioTotal());
        reserva.setEstado(Reserva.EstadoReserva.PENDIENTE);
        return reserva;
    }
} 