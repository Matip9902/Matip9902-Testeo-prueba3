# Bibliotech - Microservicios Spring Boot

Bibliotech es un sistema de gestion de biblioteca desarrollado con arquitectura de microservicios. Permite administrar autores, libros, clientes, sucursales, empleados, prestamos, multas y reservas.

El proyecto esta preparado para ejecutarse con Docker Compose. Las peticiones externas entran por el API Gateway en `localhost:9090`; los microservicios trabajan dentro de la red interna con puertos dinamicos.

## Integrantes

- Matias Imil
- Giovanni Orellana: [Hikarifire](https://github.com/Hikarifire)

## Prueba rapida

Desde la carpeta raiz del proyecto:

```bash
docker compose down
docker compose up --build
```

En otra terminal, revisar que los contenedores esten levantados:

```bash
docker ps
```

Abrir en el navegador:

```text
http://localhost:9090
http://localhost:9090/swagger-ui/index.html
http://localhost:9090/api/v1/autores
```

Si Swagger muestra `Fetch error` justo despues de iniciar Docker, esperar unos segundos y recargar la pagina. El Gateway puede quedar disponible antes de que todos los microservicios terminen de registrarse en Eureka.

Para ver Eureka, usar el puerto que aparezca en `docker ps` en la fila `eureka-service`. Ejemplo:

```text
0.0.0.0:32768->56231/tcp
```

En ese caso se abre:

```text
http://localhost:32768
```

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
Cliente / Swagger / Postman
          |
          v
API Gateway localhost:9090
          |
          v
Eureka + microservicios internos
          |
          v
MySQL + Flyway
```

El API Gateway concentra las rutas publicas y la documentacion Swagger. Eureka permite que los servicios se registren y sean ubicados por nombre dentro del ecosistema.

## Microservicios

| Servicio | Responsabilidad |
| --- | --- |
| `api-gateway` | Entrada principal, rutas y Swagger centralizado |
| `eureka-service` | Registro y descubrimiento de servicios |
| `config-server` | Configuracion centralizada |
| `autor-service` | Gestion de autores |
| `libros-service` | Gestion de libros y consulta de autores |
| `cliente-service` | Gestion de clientes |
| `sucursal-service` | Gestion de sucursales |
| `empleado-service` | Gestion de empleados |
| `prestamos-service` | Gestion de prestamos |
| `multa-service` | Gestion de multas |
| `reserva-service` | Gestion de reservas |
| `mysql` | Base de datos relacional |

## Rutas principales

Todas las APIs se consumen desde el Gateway. `localhost:9090` es la entrada principal y redirige a Swagger:

```text
http://localhost:9090
```

| Ruta | Servicio |
| --- | --- |
| `/api/v1/autores` | `autor-service` |
| `/api/v1/libros` | `libros-service` |
| `/api/v1/clientes` | `cliente-service` |
| `/api/v1/sucursales` | `sucursal-service` |
| `/api/v1/empleados` | `empleado-service` |
| `/api/v1/prestamos` | `prestamos-service` |
| `/api/v1/multas` | `multa-service` |
| `/api/v1/reservas` | `reserva-service` |

## Accesos utiles

| Recurso | URL |
| --- | --- |
| Swagger centralizado | `http://localhost:9090/swagger-ui/index.html` |
| Eureka Dashboard | Revisar el puerto asignado con `docker ps` en la fila `eureka-service` |

## Swagger

La documentacion se revisa desde el API Gateway:

```text
http://localhost:9090/swagger-ui/index.html
```

Servicios documentados en Swagger:

| Documentacion | API Docs |
| --- | --- |
| Autor Service | `http://localhost:9090/autor-service/v3/api-docs` |
| Sucursal Service | `http://localhost:9090/sucursal-service/v3/api-docs` |
| Cliente Service | `http://localhost:9090/clientes-service/v3/api-docs` |
| Libros Service | `http://localhost:9090/libros-service/v3/api-docs` |

Las documentaciones incluyen parametros, codigos HTTP, ejemplos JSON y responsable en la cabecera OpenAPI.

## Base de datos

MySQL se inicializa con el script ubicado en:

```text
init-db/01-create-databases.sql
```

Cada microservicio aplica sus migraciones con Flyway desde:

```text
src/main/resources/db/migration
```

La configuracion JPA trabaja con validacion del esquema:

```yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

## Despliegue con Docker

Desde la raiz del proyecto:

```powershell
docker compose up --build
```

Para ejecutar en segundo plano:

```powershell
docker compose up --build -d
```

Para detener:

```powershell
docker compose down
```

Para reiniciar tambien la base de datos:

```powershell
docker compose down --volumes
docker compose up --build
```

Despues de iniciar, el sistema se consume desde:

```text
http://localhost:9090
```

Si el puerto `9090` esta ocupado en el equipo, se puede cambiar el puerto externo del Gateway en el archivo `.env`:

```env
API_GATEWAY_PORT=9091
```

Luego se reinicia Docker Compose y el acceso quedaria en `http://localhost:9091`.

## Ejecucion local

El proyecto tambien puede levantarse de forma local usando una base MySQL instalada en el equipo, por ejemplo con XAMPP.

Pasos generales:

1. Levantar MySQL.
2. Ejecutar el script `create_databases_xampp.sql`.
3. Iniciar `eureka-service`.
4. Iniciar `config-server`.
5. Iniciar `api-gateway`.
6. Iniciar los microservicios necesarios.

Los archivos de configuracion estan en formato `.yml`. Los microservicios de negocio usan puertos dinamicos mediante `server.port=0`.

## Pruebas

Las APIs documentadas tienen pruebas en capa de servicio y controlador.

| Servicio | Pruebas |
| --- | --- |
| `autor-service` | `AutorServiceTest`, `AutorControllerTest` |
| `sucursal-service` | `SucursalServiceTest`, `SucursalControllerTest` |
| `cliente-service` | `ClienteServiceTest`, `ClienteControllerTest` |
| `libros-service` | `LibroServiceTest`, `LibroControllerTest` |

Ejecucion por servicio:

```powershell
cd autor-service
mvn test
```

```powershell
cd sucursal-service
mvn test
```

```powershell
cd cliente-service
mvn test
```

```powershell
cd libros-service
mvn test
```

## Verificacion rapida

Con Docker levantado, se pueden probar estos casos desde Swagger o Postman:

| Caso | URL | Esperado |
| --- | --- | --- |
| Buscar autor existente | `GET http://localhost:9090/api/v1/autores/1` | `200 OK` |
| Buscar autor inexistente | `GET http://localhost:9090/api/v1/autores/99999` | `404 Not Found` |
| Buscar autor con id invalido | `GET http://localhost:9090/api/v1/autores/0` | `400 Bad Request` |
| Buscar sucursal existente | `GET http://localhost:9090/api/v1/sucursales/1` | `200 OK` |
| Buscar cliente con id invalido | `GET http://localhost:9090/api/v1/clientes/0` | `400 Bad Request` |
| Buscar libro con id invalido | `GET http://localhost:9090/api/v1/libros/0` | `400 Bad Request` |

Swagger y Eureka pueden tardar unos segundos en mostrar todos los servicios despues de iniciar los contenedores.
