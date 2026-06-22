package cl.duoc.sucursal_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La comuna es obligatoria.")
    private String comuna;

    @NotBlank(message = "La direccion es obligatoria.")
    private String direccion;

    @NotNull(message = "La cantidad de empleados es obligatoria.")
    @Min(value = 0, message = "La cantidad de empleados no puede ser negativa.")
    private Integer cantidadEmpleados;
}
