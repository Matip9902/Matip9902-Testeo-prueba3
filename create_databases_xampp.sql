-- Script para crear las bases de datos de Bibliotech usando MySQL local/XAMPP.
-- Ejecutar en phpMyAdmin o en la consola de MySQL antes de levantar los microservicios sin Docker.
-- Las tablas y datos iniciales los crea Flyway al iniciar cada microservicio.

CREATE DATABASE IF NOT EXISTS bd_autores_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_clientes_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_empleados_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_libros_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_multas_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_prestamos_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_reservas_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_sucursales_bibliotech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Usuario opcional para XAMPP.
-- Si usas root sin password, no necesitas ejecutar estas lineas.
-- Si quieres un usuario propio, descomentalas y ajusta la password.
--
-- CREATE USER IF NOT EXISTS 'bibliotech'@'localhost' IDENTIFIED BY 'bibliotech123';
-- GRANT ALL PRIVILEGES ON bd_autores_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_clientes_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_empleados_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_libros_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_multas_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_prestamos_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_reservas_bibliotech.* TO 'bibliotech'@'localhost';
-- GRANT ALL PRIVILEGES ON bd_sucursales_bibliotech.* TO 'bibliotech'@'localhost';
-- FLUSH PRIVILEGES;
