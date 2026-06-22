package cl.duoc.empleado_service.client;

import cl.duoc.empleado_service.dto.SucursalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sucursal-service")
public interface SucursalClient {
    @GetMapping("/api/v1/sucursales/{id}")
    SucursalDTO buscarPorId(@PathVariable Long id);
}