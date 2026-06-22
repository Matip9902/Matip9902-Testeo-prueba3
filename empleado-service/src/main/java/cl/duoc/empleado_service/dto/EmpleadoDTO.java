package cl.duoc.empleado_service.dto;

import lombok.Data;

@Data
public class EmpleadoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String cargo;
    private SucursalDTO sucursal;
}