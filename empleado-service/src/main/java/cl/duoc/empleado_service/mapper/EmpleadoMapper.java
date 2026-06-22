package cl.duoc.empleado_service.mapper;

import cl.duoc.empleado_service.dto.EmpleadoDTO;
import cl.duoc.empleado_service.dto.SucursalDTO;
import cl.duoc.empleado_service.model.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {
    public EmpleadoDTO toDTO(Empleado empleado, SucursalDTO sucursal) {
        if (empleado == null) return null;
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setEdad(empleado.getEdad());
        dto.setEmail(empleado.getEmail());
        dto.setCargo(empleado.getCargo());
        dto.setSucursal(sucursal);
        return dto;
    }
}