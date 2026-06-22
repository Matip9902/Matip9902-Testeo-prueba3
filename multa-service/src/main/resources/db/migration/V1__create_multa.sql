CREATE TABLE IF NOT EXISTS multa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_prestamo BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    fecha_devolucion_acordada DATE NOT NULL,
    fecha_devolucion_real DATE NOT NULL,
    fecha_generacion DATE NOT NULL,
    total_dias INT NOT NULL,
    monto_por_dia DOUBLE NOT NULL,
    monto_total DOUBLE NOT NULL,
    estado VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
