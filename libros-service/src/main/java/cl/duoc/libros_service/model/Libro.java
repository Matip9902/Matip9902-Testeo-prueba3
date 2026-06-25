package cl.duoc.libros_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(description = "Entidad que representa un libro del catalogo Bibliotech.")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico generado por la base de datos.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El titulo del libro es obligatorio.")
    @Schema(description = "Titulo del libro.", example = "Cien anos de soledad", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @NotNull(message = "El ID del autor es obligatorio.")
    @Positive(message = "El ID del autor debe ser positivo.")
    @Schema(description = "Identificador del autor asociado al libro.", example = "1", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idAutor;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Schema(description = "Cantidad disponible del libro.", example = "10", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long stock;
}