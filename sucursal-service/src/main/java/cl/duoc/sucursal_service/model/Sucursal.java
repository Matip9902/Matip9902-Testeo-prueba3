package cl.duoc.sucursal_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa una sucursal de Bibliotech.")
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico generado por la base de datos.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "La comuna es obligatoria.")
    @Schema(description = "Comuna donde se ubica la sucursal.", example = "Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comuna;

    @NotBlank(message = "La direccion es obligatoria.")
    @Schema(description = "Direccion fisica de la sucursal.", example = "Av. Libertador 1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccion;

    @NotNull(message = "La cantidad de empleados es obligatoria.")
    @Min(value = 0, message = "La cantidad de empleados no puede ser negativa.")
    @Schema(description = "Cantidad de empleados asignados a la sucursal.", example = "5", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadEmpleados;
}
