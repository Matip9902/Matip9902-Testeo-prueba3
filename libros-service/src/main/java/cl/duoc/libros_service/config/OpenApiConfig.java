package cl.duoc.libros_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI librosServiceOpenApi() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9090")
                                .description("API Gateway centralizado")
                ))
                .info(new Info()
                        .title("Libros Service API")
                        .version("1.0")
                        .contact(new Contact().name("Matias Imil"))
                        .description("Documentacion de endpoints para gestionar libros de Bibliotech. Ruta por Gateway: http://localhost:9090/api/v1/libros"));
    }
}
