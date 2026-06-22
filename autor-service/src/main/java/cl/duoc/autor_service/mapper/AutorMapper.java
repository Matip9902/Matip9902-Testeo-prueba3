package cl.duoc.autor_service.mapper;

import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.model.Autor;
import org.springframework.stereotype.Component;

@Component
public class AutorMapper {
    public AutorDTO toDTO(Autor autor) {
        if (autor == null) return null;
        AutorDTO dto = new AutorDTO();
        dto.setId(autor.getId());
        dto.setNombre(autor.getNombre());
        dto.setApellido(autor.getApellido());
        dto.setNacionalidad(autor.getNacionalidad());
        return dto;
    }
}