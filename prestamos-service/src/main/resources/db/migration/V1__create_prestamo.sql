CREATE TABLE IF NOT EXISTS prestamo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_libro BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NOT NULL,
    estado VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
