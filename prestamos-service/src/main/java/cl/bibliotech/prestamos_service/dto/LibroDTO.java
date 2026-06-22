package cl.bibliotech.prestamos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private Long id;
    private String titulo;
    private Object autor;
    private Long stock;
}
