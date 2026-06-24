# Bibliotech - Microservicios

Bibliotech es un sistema de gestion de biblioteca desarrollado con Spring Boot y una arquitectura de microservicios.

El proyecto se levanta con Docker Compose e incluye servicios de negocio como autores, libros, clientes, prestamos, multas, reservas, sucursales y empleados. Tambien usa servicios de apoyo como Eureka, Config Server, API Gateway y MySQL.

## Tecnologias principales

- Java 17
- Spring Boot
- Spring Cloud
- Spring Cloud Gateway
- Eureka Server
- Config Server
- OpenFeign
- Spring Data JPA
- MySQL 8.0
- Flyway
- Docker Compose
- Swagger / OpenAPI

## Arquitectura general

```text
Usuario / Postman / Navegador
        |
        v
   API Gateway
        |
        v
 Microservicios
        |
        v
      MySQL
```

Eureka permite que los servicios se registren y puedan encontrarse por nombre dentro de la red Docker.

Nota util: el Gateway es la entrada principal. No es necesario abrir cada microservicio desde fuera si la ruta ya esta configurada en el Gateway.

## Servicios del proyecto

| Servicio | Funcion |
| --- | --- |
| `api-gateway` | Entrada principal a las APIs |
| `eureka-service` | Registro y descubrimiento de servicios |
| `config-server` | Configuracion centralizada |
| `autor-service` | Gestion de autores |
| `libros-service` | Gestion de libros |
| `cliente-service` | Gestion de clientes |
| `prestamos-service` | Gestion de prestamos |
| `multa-service` | Gestion de multas |
| `reserva-service` | Gestion de reservas |
| `sucursal-service` | Gestion de sucursales |
| `empleado-service` | Gestion de empleados |
| `mysql` | Base de datos |

Algunos microservicios no muestran un puerto externo en Docker. Eso es normal: quedan disponibles dentro de la red interna y se consumen mediante el API Gateway o por comunicacion entre servicios.

## Levantar el proyecto

Desde la raiz del proyecto:

```powershell
docker compose up --build
```

Para dejarlo corriendo en segundo plano:

```powershell
docker compose up --build -d
```

Para detenerlo:

```powershell
docker compose down
```

Si se necesita reiniciar tambien la base de datos:

```powershell
docker compose down --volumes
docker compose up --build
```

Nota util: `--volumes` borra los datos de MySQL, por eso Flyway vuelve a crear tablas y datos iniciales.

## Ver puertos

Los puertos externos se asignan automaticamente porque en el `.env` se usan valores en `0`.

Para ver que puerto quedo asignado:

```powershell
docker ps
```

Ejemplo:

```text
0.0.0.0:32804->62000/tcp   api-gateway
0.0.0.0:32801->8080/tcp    autor-service
```

En ese caso:

```text
Gateway: http://localhost:32804
Autores: http://localhost:32801
```

## Usar el API Gateway

La forma recomendada de consumir el sistema es usando el puerto externo del `api-gateway`.

Ejemplo:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
http://localhost:PUERTO_GATEWAY/api/v1/libros
http://localhost:PUERTO_GATEWAY/api/v1/clientes
http://localhost:PUERTO_GATEWAY/api/v1/sucursales
```

Nota util: el puerto interno del Gateway es `62000`, pero desde el navegador se usa el puerto externo que aparece en Docker.

## Rutas principales

| Ruta | Servicio |
| --- | --- |
| `/api/v1/autores` | `autor-service` |
| `/api/v1/libros` | `libros-service` |
| `/api/v1/clientes` | `cliente-service` |
| `/api/v1/prestamos` | `prestamos-service` |
| `/api/v1/multas` | `multa-service` |
| `/api/v1/reservas` | `reserva-service` |
| `/api/v1/sucursales` | `sucursal-service` |
| `/api/v1/empleados` | `empleado-service` |

## Eureka

Eureka muestra los servicios registrados.

Primero revisar el puerto:

```powershell
docker ps
```

Luego abrir:

```text
http://localhost:PUERTO_EUREKA
```

Nota util: si un servicio no aparece de inmediato, puede tardar unos segundos mientras termina de iniciar.

## Swagger

Swagger permite ver y probar endpoints desde el navegador.

Los servicios documentados directamente son:

- `autor-service`
- `sucursal-service`
- `cliente-service`
- `libros-service`

Formato:

```text
http://localhost:PUERTO_SERVICIO/swagger-ui/index.html
```

Ejemplo:

```text
http://localhost:32801/swagger-ui/index.html
```

En Swagger se pueden revisar:

- Metodos HTTP (`GET`, `POST`, `PUT`, `DELETE`)
- Parametros
- Codigos de respuesta
- Ejemplos JSON

## Base de datos y Flyway

El proyecto usa MySQL dentro de Docker.

Cada microservicio trabaja con su propia base de datos. Flyway se encarga de ejecutar scripts SQL versionados para crear tablas y cargar datos iniciales.

Ejemplo de idea:

```text
V1__create_tables.sql
V2__insert_data.sql
```

Nota util: Flyway ayuda a que otra persona pueda levantar el proyecto y tener la base creada automaticamente.

## Feign

Algunos servicios usan OpenFeign para comunicarse con otros microservicios.

La idea es evitar escribir llamadas HTTP manuales. Con Feign se define una interfaz y Spring se encarga de hacer la llamada al servicio correspondiente.

Nota util: Feign se integra con Eureka, por eso los servicios pueden llamarse por nombre.

## Prueba rapida

1. Levantar el proyecto:

```powershell
docker compose up --build -d
```

2. Ver puertos:

```powershell
docker ps
```

3. Abrir Eureka:

```text
http://localhost:PUERTO_EUREKA
```

4. Probar una ruta desde el Gateway:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
```

5. Abrir Swagger de un servicio:

```text
http://localhost:PUERTO_SERVICIO/swagger-ui/index.html
```

## Puntos clave

- Docker Compose levanta todos los servicios juntos.
- Eureka muestra que los microservicios estan registrados.
- El API Gateway es la puerta de entrada.
- Swagger muestra la documentacion y permite probar endpoints.
- Flyway crea la estructura de la base de datos automaticamente.
- Feign permite comunicacion entre microservicios.

## Problemas comunes

### Un servicio no muestra puerto externo

No siempre es un problema. Puede estar pensado para funcionar solo dentro de Docker y ser consumido desde el Gateway.

### El Gateway no responde

Revisar el puerto real con:

```powershell
docker ps
```

Y usar:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
```

### Swagger no abre

Revisar que el servicio tenga puerto externo publicado y usar:

```text
http://localhost:PUERTO_SERVICIO/swagger-ui/index.html
```

### La base de datos queda con datos antiguos

Reiniciar volumenes:

```powershell
docker compose down --volumes
docker compose up --build
```
