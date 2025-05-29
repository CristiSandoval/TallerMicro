package com.rentacar.cliente.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ClienteDTO {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @NotBlank(message = "El número de licencia es obligatorio")
    @Pattern(regexp = "^[A-Z0-9]{6,12}$", message = "El número de licencia debe tener entre 6 y 12 caracteres alfanuméricos")
    private String numeroLicencia;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{8,12}$", message = "El teléfono debe tener entre 8 y 12 dígitos")
    private String telefono;
    
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;
    
    private LocalDate fechaRegistro;
    private boolean activo;
} 