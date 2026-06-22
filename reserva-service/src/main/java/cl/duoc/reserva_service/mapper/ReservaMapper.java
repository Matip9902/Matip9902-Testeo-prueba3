package cl.duoc.reserva_service.mapper;

import cl.duoc.reserva_service.dto.ReservaDTO;
import cl.duoc.reserva_service.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaDTO toDTO(Reserva reserva) {
        if (reserva == null) return null;
        return new ReservaDTO(
                reserva.getId(),
                reserva.getIdCliente(),
                reserva.getIdLibro(),
                reserva.getIdEmpleado(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );
    }

    public Reserva toEntity(ReservaDTO dto) {
        if (dto == null) return null;
        return new Reserva(
                dto.getId(),
                dto.getIdCliente(),
                dto.getIdLibro(),
                dto.getIdEmpleado(),
                dto.getFechaReserva(),
                dto.getEstado()
        );
    }
}