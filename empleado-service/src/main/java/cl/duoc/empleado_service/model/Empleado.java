package cl.duoc.empleado_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del empleado es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido del empleado es obligatorio.")
    private String apellido;

    @NotNull(message = "La edad es obligatoria.")
    @Min(value = 18, message = "El empleado debe ser mayor de edad.")
    @Max(value = 75, message = "La edad no puede superar 75 anos.")
    private Integer edad;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email debe tener un formato valido.")
    private String email;

    @NotBlank(message = "El cargo es obligatorio.")
    private String cargo;

    @NotNull(message = "El ID de sucursal es obligatorio.")
    @Positive(message = "El ID de sucursal debe ser positivo.")
    private Long idSucursal;
}
