package cl.duoc.multa_service.client;

import cl.duoc.multa_service.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-service")
public interface ClienteClient {
    @GetMapping("/api/v1/clientes/{id}")
    ClienteDTO buscarPorId(@PathVariable Long id);
}