# Bibliotech - Proyecto Semestral Full Stack I

Sistema de gestion bibliotecaria basado en microservicios con Spring Boot, Eureka, Config Server, API Gateway, MySQL y Flyway.

## Arquitectura

Punto de entrada unico interno:

```text
api-gateway -> puerto interno 62000
```

Servicios de infraestructura:

```text
eureka-service   -> puerto interno 56231
config-server    -> puerto interno 57184
api-gateway      -> puerto interno 62000
```

Los puertos externos se asignan automaticamente por Docker para evitar conflictos en otros computadores. Se pueden revisar con `docker compose ps`.

Microservicios de negocio con puerto interno `8080` cuando se levantan con Docker Compose:

```text
clientes-service
autor-service
libros-service
prestamos-service
multas-service
reserva-service
sucursal-service
empleado-service
```

## Base de Datos y Flyway

Cada microservicio usa MySQL y tiene su propia base de datos. Las URLs usan `createDatabaseIfNotExist=true`, por lo que MySQL crea la base automaticamente al iniciar el servicio. Flyway crea las tablas y carga 15 registros iniciales por base.

```text
bd_clientes_bibliotech
bd_autores_bibliotech
bd_libros_bibliotech
bd_prestamos_bibliotech
bd_multas_bibliotech
bd_reservas_bibliotech
bd_sucursales_bibliotech
bd_empleados_bibliotech
```

Importante: si ya habias ejecutado migraciones anteriores, elimina esas bases desde phpMyAdmin antes de probar esta version. Flyway guarda historial y puede rechazar cambios si una migracion vieja ya fue aplicada.

## Despliegue con Docker

El proyecto incluye un `Dockerfile` por cada microservicio y un archivo `docker-compose.yml` en la raiz para levantar todo el ecosistema.

### Requisitos

```text
Docker Desktop instalado y en ejecucion
Git instalado
Puertos disponibles o configurados mediante .env
```

No es necesario tener XAMPP o MySQL local encendido, porque Docker Compose levanta su propio contenedor MySQL.

### Configurar puertos

El proyecto incluye el archivo `.env.example` con los puertos publicos usados por Docker. Por defecto estan en `0`, lo que significa que Docker elegira un puerto libre automaticamente:

```text
MYSQL_PORT=0
EUREKA_PORT=0
CONFIG_SERVER_PORT=0
API_GATEWAY_PORT=0
AUTOR_SWAGGER_PORT=0
SUCURSAL_SWAGGER_PORT=0
```

Para usarlo, copia el archivo como `.env`:

```bash
copy .env.example .env
```

En Git Bash tambien puedes usar:

```bash
cp .env.example .env
```

Si quieres usar un puerto fijo, modifica el valor correspondiente en `.env`. Por ejemplo:

```text
API_GATEWAY_PORT=62001
```

Si lo dejas en `0`, Docker asignara un puerto libre y deberas consultarlo con:

```bash
docker compose ps
```

### Levantar el proyecto

Desde la carpeta raiz del proyecto ejecuta:

```text
docker compose up --build
```

Este comando construye las imagenes y levanta:

```text
mysql
eureka-service
config-server
api-gateway
autor-service
cliente-service
empleado-service
libros-service
multa-service
prestamos-service
reserva-service
sucursal-service
```

### Verificar el despliegue

Abre Eureka:

```text
http://localhost:PUERTO_EUREKA
```

El valor de `PUERTO_EUREKA` se obtiene con `docker compose ps`. Busca la fila de `eureka-service` y revisa el puerto publicado hacia `56231/tcp`.

En Eureka deben aparecer registrados los servicios:

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

El mensaje rojo de Eureka sobre renovaciones puede aparecer al inicio y es normal en ambiente local mientras los servicios terminan de renovar su estado.

### Probar el API Gateway

El punto de entrada principal es:

```text
http://localhost:PUERTO_GATEWAY
```

El valor de `PUERTO_GATEWAY` se obtiene con `docker compose ps`. Busca la fila de `api-gateway` y revisa el puerto publicado hacia `62000/tcp`.

Ejemplos:

```text
http://localhost:PUERTO_GATEWAY/api/v1/autores
http://localhost:PUERTO_GATEWAY/api/v1/clientes
http://localhost:PUERTO_GATEWAY/api/v1/libros
```

### Probar Swagger

Swagger esta expuesto en los microservicios documentados:

```text
http://localhost:PUERTO_AUTOR_SWAGGER/swagger-ui/index.html
http://localhost:PUERTO_SUCURSAL_SWAGGER/swagger-ui/index.html
```

Estos puertos tambien se revisan con `docker compose ps`, buscando `autor-service` y `sucursal-service`, ambos publicados hacia `8080/tcp`.

Desde Swagger se puede abrir un endpoint, presionar `Try it out` y luego `Execute` para probar la API.

### Revisar Flyway

Para revisar que Flyway ejecuto las migraciones, puedes ver los logs de un microservicio:

```bash
docker compose logs autor-service
```

En los logs deben aparecer mensajes como:

```text
Successfully validated 2 migrations
Current version of schema
Schema is up to date
```

Tambien se puede entrar a MySQL y revisar la tabla de historial:

```bash
docker compose exec mysql mysql -uroot
```

Luego:

```sql
USE bd_autores_bibliotech;
SELECT * FROM flyway_schema_history;
```

### Apagar el proyecto

Para detener los contenedores:

```bash
docker compose down
```

Para detenerlos y borrar tambien los datos de MySQL:

```bash
docker compose down --volumes
```

Usa `--volumes` solo cuando quieras que Flyway vuelva a crear las bases y cargar los datos desde cero.

## Endpoints GET

Todos se consumen por el Gateway:

```text
GET http://localhost:62000/api/v1/autores
GET http://localhost:62000/api/v1/clientes
GET http://localhost:62000/api/v1/libros
GET http://localhost:62000/api/v1/prestamos
GET http://localhost:62000/api/v1/multas
GET http://localhost:62000/api/v1/reservas
GET http://localhost:62000/api/v1/sucursales
GET http://localhost:62000/api/v1/empleados
```

## Prueba Rapida

1. Abre Eureka:

```text
http://localhost:56231
```

2. Verifica que aparezcan los servicios registrados.

3. Prueba:

```text
GET http://localhost:62000/api/v1/autores
GET http://localhost:62000/api/v1/clientes
GET http://localhost:62000/api/v1/libros
```

Cada tabla deberia traer 15 registros cargados por Flyway.
