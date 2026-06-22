package cl.duoc.libros_service.client;


import cl.duoc.libros_service.dto.AutorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "autor-service")
public interface AutorClient {
    @GetMapping("/api/v1/autores/{id}")
    AutorDTO buscarPorId(@PathVariable Long id);
}
