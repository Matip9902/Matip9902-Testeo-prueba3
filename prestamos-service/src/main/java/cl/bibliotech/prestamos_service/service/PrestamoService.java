package cl.bibliotech.prestamos_service.service;

import cl.bibliotech.prestamos_service.clients.ClientFeign;
import cl.bibliotech.prestamos_service.clients.LibroFeign;
import cl.bibliotech.prestamos_service.dto.ClienteDTO;
import cl.bibliotech.prestamos_service.dto.LibroDTO;
import cl.bibliotech.prestamos_service.dto.PrestamoDTO;
import cl.bibliotech.prestamos_service.mapper.PrestamoMapper;
import cl.bibliotech.prestamos_service.model.Prestamo;
import cl.bibliotech.prestamos_service.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PrestamoService {
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PrestamoMapper prestamoMapper;
    @Autowired
    private LibroFeign libroFeign;
    @Autowired
    private ClientFeign clientFeign;
    public List<PrestamoDTO> findAll() {
        return prestamoRepository.findAll().stream()
                .map(prestamo -> {
                    ClienteDTO cliente = clientFeign.obtenerCliente(prestamo.getIdCliente());
                    LibroDTO libro = libroFeign.obtenerLibro(prestamo.getIdLibro());
                    return prestamoMapper.toDTO(prestamo, cliente, libro);
                })
                .collect(Collectors.toList());
    }
    public PrestamoDTO findById(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);

        if (prestamo == null) {
            return null;
        }
        ClienteDTO clienteDTO = clientFeign.obtenerCliente(prestamo.getIdCliente());
        LibroDTO libroDTO = libroFeign.obtenerLibro(prestamo.getIdLibro());
        return prestamoMapper.toDTO(prestamo, clienteDTO, libroDTO);
    }

    public List<PrestamoDTO> findActivos() {
        return prestamoRepository.findByEstadoIgnoreCase("ACTIVO").stream()
                .map(prestamo -> {
                    ClienteDTO cliente = clientFeign.obtenerCliente(prestamo.getIdCliente());
                    LibroDTO libro = libroFeign.obtenerLibro(prestamo.getIdLibro());
                    return prestamoMapper.toDTO(prestamo, cliente, libro);
                })
                .collect(Collectors.toList());
    }

    public List<PrestamoDTO> findByCliente(Long idCliente) {
        if (idCliente == null || idCliente <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser positivo.");
        }
        return toDTOList(prestamoRepository.findByIdCliente(idCliente));
    }

    public List<PrestamoDTO> findByLibro(Long idLibro) {
        if (idLibro == null || idLibro <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser positivo.");
        }
        return toDTOList(prestamoRepository.findByIdLibro(idLibro));
    }

    public List<PrestamoDTO> findAtrasados() {
        return toDTOList(prestamoRepository.findByFechaDevolucionBeforeAndEstadoIgnoreCase(LocalDate.now(), "ACTIVO"));
    }

    public Prestamo save(Prestamo prestamo) {
        validarPrestamo(prestamo);
        return prestamoRepository.save(prestamo);
    }
    public void delete(Long id) {
        prestamoRepository.deleteById(id);
    }
    public Prestamo update(Long id, Prestamo prestamo) {
        Prestamo prestamoExistente = prestamoRepository.findById(id).orElse(null);
        if (prestamoExistente == null) {
            return null;
        }
        validarPrestamo(prestamo);
        prestamoExistente.setIdLibro(prestamo.getIdLibro());
        prestamoExistente.setIdCliente(prestamo.getIdCliente());
        prestamoExistente.setFechaPrestamo(prestamo.getFechaPrestamo());
        prestamoExistente.setFechaDevolucion(prestamo.getFechaDevolucion());
        prestamoExistente.setEstado(prestamo.getEstado());
        return prestamoRepository.save(prestamoExistente);
    }

    private void validarPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El prestamo no puede ser nulo.");
        }
        if (prestamo.getFechaPrestamo() != null && prestamo.getFechaDevolucion() != null
                && prestamo.getFechaDevolucion().isBefore(prestamo.getFechaPrestamo())) {
            throw new IllegalArgumentException("La fecha de devolucion no puede ser anterior a la fecha de prestamo.");
        }
    }

    private List<PrestamoDTO> toDTOList(List<Prestamo> prestamos) {
        return prestamos.stream()
                .map(prestamo -> {
                    ClienteDTO cliente = clientFeign.obtenerCliente(prestamo.getIdCliente());
                    LibroDTO libro = libroFeign.obtenerLibro(prestamo.getIdLibro());
                    return prestamoMapper.toDTO(prestamo, cliente, libro);
                })
                .collect(Collectors.toList());
    }
}
