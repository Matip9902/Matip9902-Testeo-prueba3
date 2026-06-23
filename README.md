# Bibliotech - Sistema de Biblioteca con Microservicios

Bibliotech es un sistema de gestion bibliotecaria desarrollado con microservicios en Spring Boot. El proyecto usa Eureka para descubrimiento de servicios, Config Server para configuracion centralizada, API Gateway como punto de entrada, MySQL como base de datos y Flyway para crear tablas y cargar datos iniciales.

## Tecnologias

- Java 17
- Spring Boot 4.0.6
- Spring Cloud 2025.1.1
- Spring Cloud Gateway
- Netflix Eureka
- Spring Cloud Config Server
- Spring Data JPA
- Spring Web MVC / WebFlux
- Spring Cloud OpenFeign
- MySQL 8.4
- Flyway
- Docker y Docker Compose
- Swagger/OpenAPI en servicios documentados

## Arquitectura

```text
Cliente / Postman / Navegador
          |
          v
   API Gateway :62000
          |
          v
      Eureka Server
          |
          v
Microservicios de negocio
          |
          v
        MySQL
```

El `api-gateway` es la puerta de entrada para consumir las APIs. Los microservicios se registran en `eureka-service` y se comunican internamente dentro de la red de Docker.

## Servicios

| Servicio | Funcion | Puerto interno | Puerto externo |
| --- | --- | --- | --- |
| `mysql` | Base de datos MySQL | `3306` | Automatico con `MYSQL_PORT=0` |
| `eureka-service` | Registro y descubrimiento de servicios | `56231` | Automatico con `EUREKA_PORT=0` |
| `config-server` | Servidor de configuracion | `57184` | Automatico con `CONFIG_SERVER_PORT=0` |
| `api-gateway` | Entrada principal a las APIs | `62000` | Automatico con `API_GATEWAY_PORT=0` |
| `autor-service` | Gestion de autores | `8080` | Automatico con `AUTOR_SWAGGER_PORT=0` |
| `sucursal-service` | Gestion de sucursales | `8080` | Automatico con `SUCURSAL_SWAGGER_PORT=0` |
| `cliente-service` | Gestion de clientes | `8080` | Solo interno |
| `libros-service` | Gestion de libros | `8080` | Solo interno |
| `prestamos-service` | Gestion de prestamos | `8080` | Solo interno |
| `multa-service` | Gestion de multas | `8080` | Solo interno |
| `reserva-service` | Gestion de reservas | `8080` | Solo interno |
| `empleado-service` | Gestion de empleados | `8080` | Solo interno |

Los servicios marcados como "Solo interno" no muestran puerto externo en Docker Desktop porque no tienen bloque `ports:` en `docker-compose.yml`. Funcionan por dentro y se consumen desde fuera mediante el `api-gateway`.

## Estructura del proyecto

```text
.
├── api-gateway
├── autor-service
├── cliente-service
├── config-microservicios
├── config-server
├── empleado-service
├── eureka-service
├── libros-service
├── multa-service
├── prestamos-service
├── reserva-service
├── sucursal-service
├── docker-compose.yml
└── .env.example
```

## Requisitos

- Docker Desktop instalado y en ejecucion.
- Git instalado.
- Conexion a internet la primera vez que se construyen las imagenes, para descargar dependencias Maven.
- No es necesario tener XAMPP ni MySQL local encendido si se usa Docker.

## Configuracion de puertos

El archivo `.env.example` define puertos externos en `0`:

```env
MYSQL_PORT=0
EUREKA_PORT=0
CONFIG_SERVER_PORT=0
API_GATEWAY_PORT=0
AUTOR_SWAGGER_PORT=0
SUCURSAL_SWAGGER_PORT=0
```

El valor `0` significa que Docker elegira automaticamente un puerto libre de tu computador. Esto evita conflictos cuando otro programa ya esta usando un puerto.

Para usar estos valores:

```powershell
copy .env.example .env
```

Tambien puedes dejar que Docker use los valores por defecto definidos en `docker-compose.yml`.

Si quieres un puerto fijo, cambia el valor en `.env`. Ejemplo:

```env
API_GATEWAY_PORT=62000
EUREKA_PORT=56231
```

## Levantar el proyecto con Docker

Desde la raiz del proyecto:

```powershell
docker compose up --build
```

Para levantarlo en segundo plano:

```powershell
docker compose up --build -d
```

Para ver los contenedores y los puertos asignados:

```powershell
docker compose ps
```

Ejemplo de salida esperada:

```text
api-gateway      32780:62000
eureka-service   32775:56231
config-server    32777:57184
autor-service    32779:8080
sucursal-service 32778:8080
```

En este ejemplo, el gateway se consume desde:

```text
http://localhost:32780
```

## Consumir APIs desde el API Gateway

Usa siempre el puerto externo del `api-gateway`. Si Docker muestra:

```text
api-gateway 32780:62000
```

entonces las rutas principales son:

```text
GET http://localhost:32780/api/v1/autores
GET http://localhost:32780/api/v1/clientes
GET http://localhost:32780/api/v1/libros
GET http://localhost:32780/api/v1/prestamos
GET http://localhost:32780/api/v1/multas
GET http://localhost:32780/api/v1/reservas
GET http://localhost:32780/api/v1/sucursales
GET http://localhost:32780/api/v1/empleados
```

La regla general es:

```text
http://localhost:PUERTO_GATEWAY/ruta-del-servicio
```

No necesitas usar los puertos internos de cada microservicio para consumir la API desde Postman o navegador.

## Rutas configuradas en el Gateway

| Ruta | Servicio destino |
| --- | --- |
| `/api/v1/autores/**` | `AUTOR-SERVICE` |
| `/api/v1/clientes/**` | `CLIENTES-SERVICE` |
| `/api/v1/libros/**` | `LIBROS-SERVICE` |
| `/api/v1/prestamos/**` | `PRESTAMOS-SERVICE` |
| `/api/v1/multas/**` | `MULTAS-SERVICE` |
| `/api/v1/reservas/**` | `RESERVA-SERVICE` |
| `/api/v1/sucursales/**` | `SUCURSAL-SERVICE` |
| `/api/v1/empleados/**` | `EMPLEADO-SERVICE` |

## Endpoints principales

### Autores

Base: `/api/v1/autores`

- `GET /api/v1/autores`
- `GET /api/v1/autores/{id}`
- `POST /api/v1/autores`
- `PUT /api/v1/autores/{id}`
- `DELETE /api/v1/autores/{id}`
- `GET /api/v1/autores/buscar`
- `GET /api/v1/autores/nacionalidad`
- `GET /api/v1/autores/apellido`
- `GET /api/v1/autores/total`

### Clientes

Base: `/api/v1/clientes`

- `GET /api/v1/clientes`
- `GET /api/v1/clientes/{id}`
- `POST /api/v1/clientes`
- `PUT /api/v1/clientes/{id}`
- `DELETE /api/v1/clientes/{id}`
- `GET /api/v1/clientes/buscar`
- `GET /api/v1/clientes/email`
- `GET /api/v1/clientes/dominio-email`
- `GET /api/v1/clientes/total`

### Libros

Base: `/api/v1/libros`

- `GET /api/v1/libros`
- `GET /api/v1/libros/{id}`
- `POST /api/v1/libros`
- `DELETE /api/v1/libros/{id}`
- `GET /api/v1/libros/buscar`
- `GET /api/v1/libros/disponibles`
- `GET /api/v1/libros/autor/{idAutor}`
- `GET /api/v1/libros/sin-stock`
- `GET /api/v1/libros/bajo-stock`
- `GET /api/v1/libros/total`

### Prestamos

Base: `/api/v1/prestamos`

- `GET /api/v1/prestamos`
- `GET /api/v1/prestamos/{id}`
- `POST /api/v1/prestamos`
- `PUT /api/v1/prestamos/{id}`
- `DELETE /api/v1/prestamos/{id}`
- `GET /api/v1/prestamos/activos`
- `GET /api/v1/prestamos/cliente/{idCliente}`
- `GET /api/v1/prestamos/libro/{idLibro}`
- `GET /api/v1/prestamos/atrasados`

### Multas

Base: `/api/v1/multas`

- `GET /api/v1/multas`
- `GET /api/v1/multas/{id}`
- `POST /api/v1/multas`
- `POST /api/v1/multas/generar/{idPrestamo}`
- `PUT /api/v1/multas/pagar/{id}`
- `GET /api/v1/multas/cliente/{idCliente}`
- `GET /api/v1/multas/pendientes`
- `GET /api/v1/multas/pagadas`
- `GET /api/v1/multas/total-pendiente`
- `GET /api/v1/multas/generadas`

### Reservas

Base: `/api/v1/reservas`

- `GET /api/v1/reservas`
- `GET /api/v1/reservas/{id}`
- `POST /api/v1/reservas`
- `PUT /api/v1/reservas/{id}`
- `DELETE /api/v1/reservas/{id}`
- `GET /api/v1/reservas/cliente/{idCliente}`
- `GET /api/v1/reservas/libro/{idLibro}`
- `GET /api/v1/reservas/estado`
- `GET /api/v1/reservas/activas`
- `GET /api/v1/reservas/fechas`

### Sucursales

Base: `/api/v1/sucursales`

- `GET /api/v1/sucursales`
- `GET /api/v1/sucursales/{id}`
- `POST /api/v1/sucursales`
- `PUT /api/v1/sucursales/{id}`
- `DELETE /api/v1/sucursales/{id}`
- `GET /api/v1/sucursales/buscar`
- `GET /api/v1/sucursales/con-empleados`
- `GET /api/v1/sucursales/sin-empleados`
- `GET /api/v1/sucursales/dotacion-hasta`
- `GET /api/v1/sucursales/total`

### Empleados

Base: `/api/v1/empleados`

- `GET /api/v1/empleados`
- `GET /api/v1/empleados/{id}`
- `POST /api/v1/empleados`
- `PUT /api/v1/empleados/{id}`
- `DELETE /api/v1/empleados/{id}`
- `GET /api/v1/empleados/sucursal/{idSucursal}`
- `GET /api/v1/empleados/cargo`
- `GET /api/v1/empleados/edad-desde`
- `GET /api/v1/empleados/dominio-email`
- `GET /api/v1/empleados/total`

## Eureka

Para abrir Eureka, revisa el puerto publicado:

```powershell
docker compose ps
```

Si aparece:

```text
eureka-service 32775:56231
```

abre:

```text
http://localhost:32775
```

En Eureka deberian aparecer registrados:

```text
API-GATEWAY
AUTOR-SERVICE
CLIENTES-SERVICE
EMPLEADO-SERVICE
LIBROS-SERVICE
MULTAS-SERVICE
PRESTAMOS-SERVICE
RESERVA-SERVICE
SUCURSAL-SERVICE
```

## Swagger

Los servicios con Swagger publicado directamente son:

```text
autor-service
sucursal-service
```

Primero revisa los puertos:

```powershell
docker compose ps
```

Luego abre:

```text
http://localhost:PUERTO_AUTOR/swagger-ui/index.html
http://localhost:PUERTO_SUCURSAL/swagger-ui/index.html
```

Ejemplo:

```text
http://localhost:32779/swagger-ui/index.html
```

## Base de datos

Cada microservicio usa su propia base de datos dentro del contenedor MySQL:

| Servicio | Base de datos |
| --- | --- |
| `autor-service` | `bd_autores_bibliotech` |
| `cliente-service` | `bd_clientes_bibliotech` |
| `libros-service` | `bd_libros_bibliotech` |
| `prestamos-service` | `bd_prestamos_bibliotech` |
| `multa-service` | `bd_multas_bibliotech` |
| `reserva-service` | `bd_reservas_bibliotech` |
| `sucursal-service` | `bd_sucursales_bibliotech` |
| `empleado-service` | `bd_empleados_bibliotech` |

Las URLs usan `createDatabaseIfNotExist=true`, por lo que MySQL crea cada base automaticamente al iniciar el servicio.

Flyway ejecuta migraciones `V1__create_*.sql` y `V2__insert_*.sql`. En general, cada servicio carga 15 registros iniciales.

## Ver logs

Ver todos los logs:

```powershell
docker compose logs -f
```

Ver logs de un servicio:

```powershell
docker compose logs -f api-gateway
docker compose logs -f eureka-service
docker compose logs -f autor-service
```

## Apagar el proyecto

Detener contenedores:

```powershell
docker compose down
```

Detener y borrar datos de MySQL:

```powershell
docker compose down --volumes
```

Usa `--volumes` cuando quieras que Flyway vuelva a crear las bases desde cero.

## Problemas comunes

### Algunos servicios no muestran puerto en Docker Desktop

Es normal. Servicios como `cliente-service`, `libros-service`, `prestamos-service`, `multa-service`, `reserva-service` y `empleado-service` no publican puerto externo. Se consumen mediante el `api-gateway`.

### El gateway no responde

Revisa el puerto externo real:

```powershell
docker compose ps
```

Luego usa:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
```

### Eureka no muestra todos los servicios al instante

Es normal que algunos tarden unos segundos. Espera a que MySQL este saludable y a que los microservicios terminen de iniciar.

### Flyway falla por migraciones antiguas

Si ya habias levantado una version anterior, puede quedar historial de Flyway. Para reiniciar las bases:

```powershell
docker compose down --volumes
docker compose up --build
```

### Quiero usar XAMPP en vez de Docker

Se puede usar XAMPP solo para MySQL, pero no reemplaza a los microservicios. Tendrias que levantar cada proyecto Spring Boot manualmente con Java/Maven, cambiar las URLs de base de datos de `mysql:3306` a `localhost:3306` y asignar puertos distintos a cada servicio, porque fuera de Docker no pueden compartir el mismo puerto `8080`.

Para este proyecto, Docker Compose es la forma recomendada.

## Flujo rapido de prueba

1. Levantar todo:

```powershell
docker compose up --build -d
```

2. Ver puertos:

```powershell
docker compose ps
```

3. Abrir Eureka:

```text
http://localhost:PUERTO_EUREKA
```

4. Probar el gateway:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
http://localhost:PUERTO_GATEWAY/api/v1/clientes
http://localhost:PUERTO_GATEWAY/api/v1/libros
```

5. Apagar:

```powershell
docker compose down
```
