package cl.duoc.autor_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un autor de libros.")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico generado por la base de datos.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre del autor es obligatorio.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    @Schema(description = "Nombre del autor.", example = "Gabriel", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido del autor es obligatorio.")
    @Size(max = 80, message = "El apellido no puede superar 80 caracteres.")
    @Schema(description = "Apellido del autor.", example = "Garcia Marquez", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotBlank(message = "La nacionalidad es obligatoria.")
    @Size(max = 60, message = "La nacionalidad no puede superar 60 caracteres.")
    @Schema(description = "Nacionalidad del autor.", example = "Colombiana", maxLength = 60, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nacionalidad;
}
