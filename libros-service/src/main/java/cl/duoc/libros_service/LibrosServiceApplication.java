package cl.duoc.libros_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class LibrosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrosServiceApplication.class, args);
	}

}
