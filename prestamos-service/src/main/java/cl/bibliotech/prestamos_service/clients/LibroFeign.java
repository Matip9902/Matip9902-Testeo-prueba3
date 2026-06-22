package cl.bibliotech.prestamos_service.clients;

import cl.bibliotech.prestamos_service.dto.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "libros-service")
public interface LibroFeign {
    @GetMapping("/api/v1/libros/{id}")
    LibroDTO obtenerLibro(@PathVariable("id") Long id);
}