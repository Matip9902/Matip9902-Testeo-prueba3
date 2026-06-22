package cl.duoc.multa_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrestamoDTO {
    private Long id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private String estado;
    private ClienteDTO cliente;
    private LibroDTO libro;

}
