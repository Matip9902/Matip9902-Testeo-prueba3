package cl.duoc.sucursal_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de respuesta de una sucursal registrada en Bibliotech.")
public class SucursalDTO {
    @Schema(description = "Identificador unico de la sucursal.", example = "1")
    private Long id;

    @Schema(description = "Comuna donde se encuentra la sucursal.", example = "Santiago")
    private String comuna;

    @Schema(description = "Direccion fisica de la sucursal.", example = "Av. Libertador 1234")
    private String direccion;

    @Schema(description = "Cantidad de empleados asignados a la sucursal.", example = "5")
    private Integer cantidadEmpleados;
}
