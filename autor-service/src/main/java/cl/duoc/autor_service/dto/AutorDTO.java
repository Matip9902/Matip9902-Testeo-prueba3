package cl.duoc.autor_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de respuesta de un autor registrado en Bibliotech.")
public class AutorDTO {
    @Schema(description = "Identificador unico del autor.", example = "1")
    private Long id;

    @Schema(description = "Nombre del autor.", example = "Gabriel")
    private String nombre;

    @Schema(description = "Apellido del autor.", example = "Garcia Marquez")
    private String apellido;

    @Schema(description = "Nacionalidad del autor.", example = "Colombiana")
    private String nacionalidad;
}
