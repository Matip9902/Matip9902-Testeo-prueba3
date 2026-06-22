package cl.duoc.sucursal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SucursalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SucursalServiceApplication.class, args);
	}

}
