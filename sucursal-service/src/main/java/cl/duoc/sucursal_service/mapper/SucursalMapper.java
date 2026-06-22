package cl.duoc.sucursal_service.mapper;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.model.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public SucursalDTO toDTO(Sucursal sucursal) {
        if (sucursal == null) return null;
        SucursalDTO dto = new SucursalDTO();
        dto.setId(sucursal.getId());
        dto.setComuna(sucursal.getComuna());
        dto.setDireccion(sucursal.getDireccion());
        dto.setCantidadEmpleados(sucursal.getCantidadEmpleados());
        return dto;
    }
}