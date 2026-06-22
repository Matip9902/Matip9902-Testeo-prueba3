package cl.duoc.autor_service.repository;

import cl.duoc.autor_service.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    List<Autor> findByNombreContainingIgnoreCase(String nombre);
    List<Autor> findByNacionalidad(String nacionalidad);
    List<Autor> findByApellidoContainingIgnoreCase(String apellido);
}
