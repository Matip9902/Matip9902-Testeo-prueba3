package cl.duoc.libros_service.repository;

import cl.duoc.libros_service.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    List<Libro> findByIdAutor(Long idAutor);
    List<Libro> findByStockGreaterThan(Long stock);
    List<Libro> findByStock(Long stock);
    List<Libro> findByStockLessThanEqual(Long stock);
}
