package cl.bibliotech.prestamos_service.repository;
import cl.bibliotech.prestamos_service.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByEstadoIgnoreCase(String estado);
    List<Prestamo> findByIdCliente(Long idCliente);
    List<Prestamo> findByIdLibro(Long idLibro);
    List<Prestamo> findByFechaDevolucionBeforeAndEstadoIgnoreCase(java.time.LocalDate fecha, String estado);
}
