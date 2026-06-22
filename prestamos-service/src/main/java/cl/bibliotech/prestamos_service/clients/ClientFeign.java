package cl.bibliotech.prestamos_service.clients;

import cl.bibliotech.prestamos_service.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-service")
public interface ClientFeign {

    @GetMapping("/api/v1/clientes/{id}")
    ClienteDTO obtenerCliente(@PathVariable("id") Long id);
}