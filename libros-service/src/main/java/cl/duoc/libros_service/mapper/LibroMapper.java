package cl.duoc.libros_service.mapper;

import cl.duoc.libros_service.dto.AutorDTO;
import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.model.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {
    public LibroDTO toDTO(Libro libro, AutorDTO autor) {
        if (libro == null) return null;
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(autor);
        dto.setStock(libro.getStock());
        return dto;
    }
}
