package cl.duoc.autor_service.dto;

import lombok.Data;

@Data
public class AutorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
}
