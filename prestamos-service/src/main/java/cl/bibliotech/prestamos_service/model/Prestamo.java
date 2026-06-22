package cl.bibliotech.prestamos_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del libro es obligatorio.")
    @Positive(message = "El ID del libro debe ser positivo.")
    private Long idLibro;

    @NotNull(message = "El ID del cliente es obligatorio.")
    @Positive(message = "El ID del cliente debe ser positivo.")
    private Long idCliente;

    @NotNull(message = "La fecha de prestamo es obligatoria.")
    private LocalDate fechaPrestamo;

    @NotNull(message = "La fecha de devolucion es obligatoria.")
    private LocalDate fechaDevolucion;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "ACTIVO|DEVUELTO|ATRASADO", message = "El estado debe ser ACTIVO, DEVUELTO o ATRASADO.")
    private String estado;

    @AssertTrue(message = "La fecha de devolucion no puede ser anterior a la fecha de prestamo.")
    public boolean isRangoFechasValido() {
        return fechaPrestamo == null || fechaDevolucion == null || !fechaDevolucion.isBefore(fechaPrestamo);
    }
}
