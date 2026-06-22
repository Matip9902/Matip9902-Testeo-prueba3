package cl.duoc.reserva_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio.")
    @Positive(message = "El ID del cliente debe ser positivo.")
    private Long idCliente;

    @NotNull(message = "El ID del libro es obligatorio.")
    @Positive(message = "El ID del libro debe ser positivo.")
    private Long idLibro;

    @NotNull(message = "El ID del empleado es obligatorio.")
    @Positive(message = "El ID del empleado debe ser positivo.")
    private Long idEmpleado;

    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "ACTIVA|CONFIRMADA|CANCELADA|COMPLETADA", message = "El estado debe ser ACTIVA, CONFIRMADA, CANCELADA o COMPLETADA.")
    private String estado;
}
