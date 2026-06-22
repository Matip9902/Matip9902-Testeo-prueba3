package cl.duoc.libros_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private Long id;
    private String titulo;
    private AutorDTO autor;  // cambia aquí
    private Long stock;
}

