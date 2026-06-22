package cl.duoc.multa_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del prestamo es obligatorio.")
    @Positive(message = "El ID del prestamo debe ser positivo.")
    private Long idPrestamo;

    @NotNull(message = "El ID del cliente es obligatorio.")
    @Positive(message = "El ID del cliente debe ser positivo.")
    private Long idCliente;

    @NotNull(message = "La fecha acordada es obligatoria.")
    private LocalDate fechaDevolucionAcordada;

    @NotNull(message = "La fecha real es obligatoria.")
    private LocalDate fechaDevolucionReal;

    @NotNull(message = "La fecha de generacion es obligatoria.")
    private LocalDate fechaGeneracion;

    @NotNull(message = "El total de dias es obligatorio.")
    @Min(value = 1, message = "El total de dias debe ser mayor a cero.")
    private Integer totalDias;

    @NotNull(message = "El monto por dia es obligatorio.")
    @Positive(message = "El monto por dia debe ser positivo.")
    private Double montoPorDia;

    @NotNull(message = "El monto total es obligatorio.")
    @PositiveOrZero(message = "El monto total no puede ser negativo.")
    private Double montoTotal;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "PENDIENTE|PAGADA", message = "El estado debe ser PENDIENTE o PAGADA.")
    private String estado;

    @AssertTrue(message = "La fecha real de devolucion no puede ser anterior a la fecha acordada.")
    public boolean isRangoFechasValido() {
        return fechaDevolucionAcordada == null || fechaDevolucionReal == null
                || !fechaDevolucionReal.isBefore(fechaDevolucionAcordada);
    }
}
