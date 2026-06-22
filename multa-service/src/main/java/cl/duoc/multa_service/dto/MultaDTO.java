package cl.duoc.multa_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MultaDTO {
    private Long id;
    private LocalDate fechaDevolucionAcordada;
    private LocalDate fechaDevolucionReal;
    private LocalDate fechaGeneracion;
    private Integer totalDias;
    private Double montoPorDia;
    private Double montoTotal;
    private String estado;
    private PrestamoDTO prestamo;
    private ClienteDTO cliente;
}
