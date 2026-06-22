package cl.duoc.reserva_service.service;

import cl.duoc.reserva_service.dto.ReservaDTO;
import cl.duoc.reserva_service.mapper.ReservaMapper;
import cl.duoc.reserva_service.model.Reserva;
import cl.duoc.reserva_service.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaMapper reservaMapper;

    public List<ReservaDTO> findAll() {
        log.info("Buscando todas las reservas registradas.");
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }

    public ReservaDTO findById(Long id) {
        log.info("Buscando reserva por ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva con ID " + id + " no encontrada."));
        return reservaMapper.toDTO(reserva);
    }

    public ReservaDTO save(Reserva reserva) {
        log.info("Validando y guardando una nueva reserva.");
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }
        if (reserva.getIdCliente() == null || reserva.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio y debe ser un número positivo.");
        }
        if (reserva.getIdLibro() == null || reserva.getIdLibro() <= 0) {
            throw new IllegalArgumentException("El ID del libro es obligatorio y debe ser un número positivo.");
        }
        if (reserva.getIdEmpleado() == null || reserva.getIdEmpleado() <= 0) {
            throw new IllegalArgumentException("El ID del empleado es obligatorio y debe ser un número positivo.");
        }
        if (reserva.getFechaReserva() == null) {
            throw new IllegalArgumentException("La fecha de reserva es obligatoria.");
        }
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public ReservaDTO update(Long id, Reserva reserva) {
        log.info("Actualizando la reserva con ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva con ID " + id + " no encontrada."));

        reservaExistente.setIdCliente(reserva.getIdCliente());
        reservaExistente.setIdLibro(reserva.getIdLibro());
        reservaExistente.setIdEmpleado(reserva.getIdEmpleado());
        reservaExistente.setFechaReserva(reserva.getFechaReserva());
        reservaExistente.setEstado(reserva.getEstado());

        return reservaMapper.toDTO(reservaRepository.save(reservaExistente));
    }

    public void delete(Long id) {
        log.info("Eliminando la reserva con ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva con ID " + id + " no encontrada.");
        }
        reservaRepository.deleteById(id);
    }

    public List<ReservaDTO> findByIdCliente(Long idCliente) {
        log.info("Filtrando reservas por el ID de Cliente: {}", idCliente);
        if (idCliente == null || idCliente <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser un número positivo.");
        }
        List<Reserva> reservas = reservaRepository.findByIdCliente(idCliente);
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }
    public List<ReservaDTO> findByIdLibro(Long idLibro) {
        if (idLibro == null || idLibro <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser positivo.");
        }
        List<Reserva> reservas = reservaRepository.findByIdLibro(idLibro);
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }

    public List<ReservaDTO> findByEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede estar vacio.");
        }
        List<Reserva> reservas = reservaRepository.findByEstadoIgnoreCase(estado);
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }

    public List<ReservaDTO> findActivas() {
        List<Reserva> reservas = reservaRepository.findByEstadoIgnoreCase("ACTIVA");
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }

    public List<ReservaDTO> findByFechaReservaEntre(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas desde y hasta son obligatorias.");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha hasta no puede ser anterior a la fecha desde.");
        }
        List<Reserva> reservas = reservaRepository.findByFechaReservaBetween(desde, hasta);
        return reservas.stream().map(reservaMapper::toDTO).toList();
    }
}
