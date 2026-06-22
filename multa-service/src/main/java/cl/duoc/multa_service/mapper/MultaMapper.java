package cl.duoc.multa_service.mapper;

import cl.duoc.multa_service.dto.ClienteDTO;
import cl.duoc.multa_service.dto.MultaDTO;
import cl.duoc.multa_service.dto.PrestamoDTO;
import cl.duoc.multa_service.model.Multa;
import org.springframework.stereotype.Component;

@Component
public class MultaMapper {
    public MultaDTO toDTO(Multa multa, PrestamoDTO prestamo, ClienteDTO cliente) {
        MultaDTO dto = new MultaDTO();
        dto.setId(multa.getId());
        dto.setFechaDevolucionAcordada(multa.getFechaDevolucionAcordada());
        dto.setFechaDevolucionReal(multa.getFechaDevolucionReal());
        dto.setFechaGeneracion(multa.getFechaGeneracion());
        dto.setTotalDias(multa.getTotalDias());
        dto.setMontoPorDia(multa.getMontoPorDia());
        dto.setMontoTotal(multa.getMontoTotal());
        dto.setEstado(multa.getEstado());
        dto.setPrestamo(prestamo);
        dto.setCliente(cliente);
        return dto;
    }
}
