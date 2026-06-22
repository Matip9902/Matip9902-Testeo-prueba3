package cl.duoc.reserva_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio.")
    private Long idCliente;

    @NotNull(message = "El ID del libro es obligatorio.")
    private Long idLibro;

    @NotNull(message = "El ID del empleado es obligatorio.")
    private Long idEmpleado;

    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @NotBlank(message = "El estado de la reserva es obligatorio.")
    @Pattern(regexp = "^(ACTIVA|CONFIRMADA|CANCELADA|COMPLETADA)$",
            message = "El estado debe ser: ACTIVA, CONFIRMADA, CANCELADA o COMPLETADA.")
    private String estado;
}
