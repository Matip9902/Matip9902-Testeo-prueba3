package cl.duoc.reserva_service.repository;

import cl.duoc.reserva_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByIdCliente(Long idCliente);
    List<Reserva> findByIdLibro(Long idLibro);
    List<Reserva> findByEstadoIgnoreCase(String estado);
    List<Reserva> findByFechaReservaBetween(java.time.LocalDate desde, java.time.LocalDate hasta);
}
