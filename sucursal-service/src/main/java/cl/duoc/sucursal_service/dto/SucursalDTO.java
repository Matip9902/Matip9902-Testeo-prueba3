package cl.duoc.sucursal_service.dto;

import lombok.Data;

@Data
public class SucursalDTO {
    private Long id;
    private String comuna;
    private String direccion;
    private Integer cantidadEmpleados;
}