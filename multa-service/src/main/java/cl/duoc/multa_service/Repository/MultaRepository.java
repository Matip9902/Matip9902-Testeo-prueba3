package cl.duoc.multa_service.Repository;

import cl.duoc.multa_service.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByIdCliente(Long idCliente);
    List<Multa> findByEstado(String estado);
    List<Multa> findByEstadoIgnoreCase(String estado);
    List<Multa> findByFechaGeneracionBetween(java.time.LocalDate desde, java.time.LocalDate hasta);
}
