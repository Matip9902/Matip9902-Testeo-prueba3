package cl.duoc.multa_service.dto;

import lombok.Data;

@Data
public class LibroDTO {
    private Long id;
    private String titulo;
    private Object autor;
    private Long stock;
}
