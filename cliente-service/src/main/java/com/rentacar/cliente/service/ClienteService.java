package com.rentacar.cliente.service;

import com.rentacar.cliente.dto.ClienteDTO;
import com.rentacar.cliente.model.Cliente;
import com.rentacar.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
    }

    @Transactional
    public ClienteDTO crear(ClienteDTO clienteDTO) {
        validarClienteNuevo(clienteDTO);
        Cliente cliente = convertirAEntidad(clienteDTO);
        return convertirADTO(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteDTO actualizar(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        
        validarClienteExistente(clienteDTO, cliente);
        actualizarEntidad(cliente, clienteDTO);
        return convertirADTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private void validarClienteNuevo(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
        }
        if (clienteRepository.existsByNumeroLicencia(clienteDTO.getNumeroLicencia())) {
            throw new IllegalArgumentException("Ya existe un cliente con el número de licencia: " + clienteDTO.getNumeroLicencia());
        }
    }

    private void validarClienteExistente(ClienteDTO clienteDTO, Cliente clienteExistente) {
        if (!clienteDTO.getEmail().equals(clienteExistente.getEmail()) &&
            clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
        }
        if (!clienteDTO.getNumeroLicencia().equals(clienteExistente.getNumeroLicencia()) &&
            clienteRepository.existsByNumeroLicencia(clienteDTO.getNumeroLicencia())) {
            throw new IllegalArgumentException("Ya existe un cliente con el número de licencia: " + clienteDTO.getNumeroLicencia());
        }
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setNumeroLicencia(cliente.getNumeroLicencia());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setActivo(cliente.isActivo());
        return dto;
    }

    private Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setNumeroLicencia(dto.getNumeroLicencia());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setActivo(true);
        return cliente;
    }

    private void actualizarEntidad(Cliente cliente, ClienteDTO dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setNumeroLicencia(dto.getNumeroLicencia());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
    }
} 