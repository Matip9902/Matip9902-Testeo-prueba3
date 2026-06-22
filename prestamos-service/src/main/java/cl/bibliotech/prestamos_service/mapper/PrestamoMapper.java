package cl.bibliotech.prestamos_service.mapper;

import cl.bibliotech.prestamos_service.dto.ClienteDTO;
import cl.bibliotech.prestamos_service.dto.LibroDTO;
import cl.bibliotech.prestamos_service.dto.PrestamoDTO;
import cl.bibliotech.prestamos_service.model.Prestamo;
import org.springframework.stereotype.Component;
@Component
public class PrestamoMapper {
    public PrestamoDTO toDTO(Prestamo prestamo, ClienteDTO cliente, LibroDTO libro) {
        if (prestamo == null) return null;
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(prestamo.getId());
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        dto.setEstado(prestamo.getEstado());
        dto.setCliente(cliente);
        dto.setLibro(libro);
        return dto;
    }
}