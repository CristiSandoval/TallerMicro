package com.rentacar.cliente.repository;

import com.rentacar.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByNumeroLicencia(String numeroLicencia);
    boolean existsByEmail(String email);
    boolean existsByNumeroLicencia(String numeroLicencia);
} 