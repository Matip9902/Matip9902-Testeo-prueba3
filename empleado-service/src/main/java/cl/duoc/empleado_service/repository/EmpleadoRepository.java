package cl.duoc.empleado_service.repository;

import cl.duoc.empleado_service.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByIdSucursal(Long idSucursal);
    List<Empleado> findByCargoContainingIgnoreCase(String cargo);
    List<Empleado> findByEdadGreaterThanEqual(Integer edad);
    List<Empleado> findByEmailEndingWithIgnoreCase(String dominio);
}
