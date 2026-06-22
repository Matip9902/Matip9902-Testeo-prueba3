package cl.duoc.multa_service.service;

import cl.duoc.multa_service.Repository.MultaRepository;
import cl.duoc.multa_service.client.ClienteClient;
import cl.duoc.multa_service.client.PrestamoClient;
import cl.duoc.multa_service.dto.ClienteDTO;
import cl.duoc.multa_service.dto.MultaDTO;
import cl.duoc.multa_service.dto.PrestamoDTO;
import cl.duoc.multa_service.mapper.MultaMapper;
import cl.duoc.multa_service.model.Multa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {
    @Autowired
    private MultaRepository multaRepository;
    @Autowired
    private PrestamoClient prestamoClient;
    @Autowired
    private ClienteClient clienteClient;
    @Autowired
    private MultaMapper multaMapper;

    private static final Double MONTO_POR_DIA = 500.0;

    public List<MultaDTO> findAll() {
        return multaRepository.findAll().stream().map(multa -> {
            PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
            ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
            return multaMapper.toDTO(multa, prestamo, cliente);
        }).toList();
    }

    public MultaDTO findById(Long id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa con ID " + id + " no encontrada."));
        PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
        ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
        return multaMapper.toDTO(multa, prestamo, cliente);
    }

    public MultaDTO save(Multa multa) {
        if (multa == null) {
            throw new IllegalArgumentException("La multa no puede ser nula.");
        }
        Multa multaGuardada = multaRepository.save(multa);
        PrestamoDTO prestamo = prestamoClient.buscarPorId(multaGuardada.getIdPrestamo());
        ClienteDTO cliente = clienteClient.buscarPorId(multaGuardada.getIdCliente());
        return multaMapper.toDTO(multaGuardada, prestamo, cliente);
    }

    public MultaDTO generarMulta(Long idPrestamo, LocalDate fechaDevolucionReal) {
        PrestamoDTO prestamo = prestamoClient.buscarPorId(idPrestamo);

        long diasAtraso = prestamo.getFechaDevolucion().until(fechaDevolucionReal).getDays();

        if (diasAtraso <= 0) {
            throw new IllegalArgumentException("No hay atraso, no se genera multa.");
        }

        Multa multa = new Multa(
                null,
                idPrestamo,
                prestamo.getCliente().getId(),
                prestamo.getFechaDevolucion(),
                fechaDevolucionReal,
                LocalDate.now(),
                (int) diasAtraso,
                MONTO_POR_DIA,
                (double) diasAtraso * MONTO_POR_DIA,
                "PENDIENTE"
        );

        Multa multaGuardada = multaRepository.save(multa);
        ClienteDTO cliente = clienteClient.buscarPorId(prestamo.getCliente().getId()); // cambia aquí
        return multaMapper.toDTO(multaGuardada, prestamo, cliente);
    }

    public MultaDTO pagarMulta(Long id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa con ID " + id + " no encontrada."));
        if (multa.getEstado().equals("PAGADA")) {
            throw new IllegalArgumentException("La multa ya fue pagada.");
        }
        multa.setEstado("PAGADA");
        Multa multaActualizada = multaRepository.save(multa);
        PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
        ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
        return multaMapper.toDTO(multaActualizada, prestamo, cliente);
    }

    public List<MultaDTO> findByCliente(Long idCliente) {
        return multaRepository.findByIdCliente(idCliente).stream().map(multa -> {
            PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
            ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
            return multaMapper.toDTO(multa, prestamo, cliente);
        }).toList();
    }

    public List<MultaDTO> findPendientes() {
        return multaRepository.findByEstado("PENDIENTE").stream().map(multa -> {
            PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
            ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
            return multaMapper.toDTO(multa, prestamo, cliente);
        }).toList();
    }



    public List<MultaDTO> findPagadas() {
        return toDTOList(multaRepository.findByEstadoIgnoreCase("PAGADA"));
    }

    public Double totalPendiente() {
        return multaRepository.findByEstadoIgnoreCase("PENDIENTE").stream()
                .mapToDouble(Multa::getMontoTotal)
                .sum();
    }

    public List<MultaDTO> findGeneradasEntre(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas desde y hasta son obligatorias.");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha hasta no puede ser anterior a la fecha desde.");
        }
        return toDTOList(multaRepository.findByFechaGeneracionBetween(desde, hasta));
    }

    private List<MultaDTO> toDTOList(List<Multa> multas) {
        return multas.stream().map(multa -> {
            PrestamoDTO prestamo = prestamoClient.buscarPorId(multa.getIdPrestamo());
            ClienteDTO cliente = clienteClient.buscarPorId(multa.getIdCliente());
            return multaMapper.toDTO(multa, prestamo, cliente);
        }).toList();
    }
}
