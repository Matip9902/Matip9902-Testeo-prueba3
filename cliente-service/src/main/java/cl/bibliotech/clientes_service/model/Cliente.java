package cl.bibliotech.clientes_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un cliente de Bibliotech.")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico generado por la base de datos.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio.")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres.")
    @Schema(description = "Nombre del cliente.", example = "Matias", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email debe tener un formato valido.")
    @Schema(description = "Correo electronico del cliente.", example = "matias@bibliotech.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "La password es obligatoria.")
    @Size(min = 6, max = 80, message = "La password debe tener entre 6 y 80 caracteres.")
    @Schema(description = "Password del cliente.", example = "secreto123", minLength = 6, maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}