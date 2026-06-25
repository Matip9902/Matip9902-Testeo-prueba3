# Bibliotech - Microservicios

Bibliotech es un sistema de gestion de biblioteca desarrollado con Spring Boot y arquitectura de microservicios. El proyecto se levanta con Docker Compose y usa API Gateway, Eureka, Config Server, MySQL, Flyway, Feign y Swagger/OpenAPI.

## Tecnologias

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Eureka Server
- Config Server
- OpenFeign
- Spring Data JPA
- MySQL 8.0
- Flyway
- Docker Compose
- Swagger / OpenAPI
- JUnit 5 y Mockito

## Arquitectura

```text
Usuario / Swagger / Postman
        |
        v
 API Gateway :9090
        |
        v
 Microservicios
        |
        v
      MySQL
```

El API Gateway es la entrada principal del sistema. Los microservicios se registran en Eureka y se comunican dentro de la red Docker.

## Servicios

| Servicio | Funcion |
| --- | --- |
| `api-gateway` | Entrada principal y Swagger centralizado |
| `eureka-service` | Registro y descubrimiento de servicios |
| `config-server` | Configuracion centralizada |
| `autor-service` | Gestion de autores |
| `libros-service` | Gestion de libros |
| `cliente-service` | Gestion de clientes |
| `sucursal-service` | Gestion de sucursales |
| `prestamos-service` | Gestion de prestamos |
| `multa-service` | Gestion de multas |
| `reserva-service` | Gestion de reservas |
| `empleado-service` | Gestion de empleados |
| `mysql` | Base de datos |

## Levantar el proyecto

Desde la raiz del proyecto:

```powershell
docker compose up --build
```

En segundo plano:

```powershell
docker compose up --build -d
```

Detener:

```powershell
docker compose down
```

Reiniciar tambien la base de datos:

```powershell
docker compose down --volumes
docker compose up --build
```

## Puertos importantes

El API Gateway queda fijo en:

```text
http://localhost:9090
```

El puerto interno del Gateway es `62000`, pero desde el navegador se usa `9090`.

Eureka puede tener puerto dinamico segun Docker. Para verlo:

```powershell
docker ps
```

## API Gateway

Rutas principales por Gateway:

| Ruta | Servicio |
| --- | --- |
| `http://localhost:9090/api/v1/autores` | `autor-service` |
| `http://localhost:9090/api/v1/libros` | `libros-service` |
| `http://localhost:9090/api/v1/clientes` | `cliente-service` |
| `http://localhost:9090/api/v1/sucursales` | `sucursal-service` |
| `http://localhost:9090/api/v1/prestamos` | `prestamos-service` |
| `http://localhost:9090/api/v1/multas` | `multa-service` |
| `http://localhost:9090/api/v1/reservas` | `reserva-service` |
| `http://localhost:9090/api/v1/empleados` | `empleado-service` |

## Swagger centralizado

La documentacion Swagger se abre desde el API Gateway:

```text
http://localhost:9090/swagger-ui/index.html
```

APIs documentadas:

- `Autor Service`
- `Sucursal Service`
- `Cliente Service`
- `Libros Service`

Esto cumple la indicacion de tener 4 documentaciones, 2 por integrante. Cada API documentada incluye operaciones, parametros, codigos HTTP y ejemplos JSON.

Endpoints internos usados por Swagger:

```text
http://localhost:9090/autor-service/v3/api-docs
http://localhost:9090/sucursal-service/v3/api-docs
http://localhost:9090/clientes-service/v3/api-docs
http://localhost:9090/libros-service/v3/api-docs
```

## Pruebas rapidas para defensa

Probar desde Swagger o navegador:

| Caso | URL | Resultado esperado |
| --- | --- | --- |
| Autor existente | `GET http://localhost:9090/api/v1/autores/1` | `200 OK` |
| Autor con ID invalido | `GET http://localhost:9090/api/v1/autores/0` | `400 Bad Request` |
| Autor inexistente | `GET http://localhost:9090/api/v1/autores/99999` | `404 Not Found` |
| Sucursal existente | `GET http://localhost:9090/api/v1/sucursales/1` | `200 OK` |
| Sucursal con ID invalido | `GET http://localhost:9090/api/v1/sucursales/0` | `400 Bad Request` |
| Cliente con ID invalido | `GET http://localhost:9090/api/v1/clientes/0` | `400 Bad Request` |
| Libro con ID invalido | `GET http://localhost:9090/api/v1/libros/0` | `400 Bad Request` |

Tambien se puede probar que Swagger este centralizado abriendo las 4 definiciones `/v3/api-docs` desde el puerto `9090`.

## Testing

Las APIs documentadas tienen pruebas en capa `service` y en capa `controller`.

| Servicio | Service test | Controller test |
| --- | --- | --- |
| `autor-service` | `AutorServiceTest` | `AutorControllerTest` |
| `sucursal-service` | `SucursalServiceTest` | `SucursalControllerTest` |
| `cliente-service` | `ClienteServiceTest` | `ClienteControllerTest` |
| `libros-service` | `LibroServiceTest` | `LibroControllerTest` |

Los tests de controller usan `MockMvcBuilders.standaloneSetup`, Mockito y validaciones, sin `@WebMvcTest`.

Ejecutar tests:

```powershell
cd autor-service
mvn test

cd ../sucursal-service
mvn test

cd ../cliente-service
mvn test

cd ../libros-service
mvn test
```

## Flyway

Flyway ejecuta scripts SQL versionados al levantar los servicios. Esto permite crear tablas y cargar datos iniciales automaticamente.

Ejemplo:

```text
V1__create_tables.sql
V2__insert_data.sql
```

Si se eliminan los volumenes de Docker, Flyway vuelve a preparar la base al iniciar.

## Feign

OpenFeign permite comunicacion entre microservicios sin escribir llamadas HTTP manuales. Por ejemplo, `libros-service` puede consultar informacion de autores mediante `autor-service`.

Esto demuestra interoperabilidad entre servicios usando nombres registrados en Eureka.

## Puntos clave para explicar

- Docker Compose levanta MySQL, Eureka, Config Server, Gateway y microservicios.
- Eureka registra servicios para que puedan encontrarse por nombre.
- El API Gateway centraliza la entrada por `localhost:9090`.
- Swagger se abre desde el Gateway y muestra las 4 APIs documentadas.
- Flyway prepara la base de datos automaticamente.
- Feign permite llamadas entre microservicios.
- Los tests validan comportamiento de service y controller.

## Problemas comunes

### Swagger demora en cargar

Puede tardar entre 30 y 90 segundos despues de levantar Docker, porque los servicios deben iniciar, conectarse a MySQL y registrarse.

### Swagger muestra 503

Revisar que el microservicio este levantado:

```powershell
docker ps
```

Tambien revisar logs:

```powershell
docker compose logs nombre-del-servicio
```

### Un servicio no muestra puerto externo

No siempre es problema. Algunos servicios quedan solo dentro de Docker y se consumen desde el Gateway.

### Datos antiguos en MySQL

```powershell
docker compose down --volumes
docker compose up --build
```
