package cl.duoc.multa_service.client;

import cl.duoc.multa_service.dto.PrestamoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "prestamos-service")
public interface PrestamoClient {
    @GetMapping("/api/v1/prestamos/{id}")
    PrestamoDTO buscarPorId(@PathVariable Long id);
}