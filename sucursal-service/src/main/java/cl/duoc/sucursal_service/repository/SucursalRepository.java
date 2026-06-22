package cl.duoc.sucursal_service.repository;

import cl.duoc.sucursal_service.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findByComunaContainingIgnoreCase(String comuna);
    List<Sucursal> findByCantidadEmpleadosGreaterThan(Integer cantidad);
    List<Sucursal> findByCantidadEmpleados(Integer cantidad);
    List<Sucursal> findByCantidadEmpleadosLessThanEqual(Integer cantidad);
}
