package cl.duoc.autor_service.model;

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
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del autor es obligatorio.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido del autor es obligatorio.")
    @Size(max = 80, message = "El apellido no puede superar 80 caracteres.")
    private String apellido;

    @NotBlank(message = "La nacionalidad es obligatoria.")
    @Size(max = 60, message = "La nacionalidad no puede superar 60 caracteres.")
    private String nacionalidad;
}
