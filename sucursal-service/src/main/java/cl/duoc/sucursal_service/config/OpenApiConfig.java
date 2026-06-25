package cl.duoc.sucursal_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sucursalServiceOpenApi() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9090")
                                .description("API Gateway centralizado"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servicio directo en ejecucion local")
                ))
                .info(new Info()
                        .title("Sucursal Service API")
                        .version("1.0")
                        .description("Documentacion de endpoints para gestionar sucursales de Bibliotech. Ruta por Gateway: http://localhost:9090/api/v1/sucursales"));
    }
}