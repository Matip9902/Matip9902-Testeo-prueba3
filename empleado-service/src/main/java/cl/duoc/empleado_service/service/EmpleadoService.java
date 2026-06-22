package cl.duoc.empleado_service.service;

import cl.duoc.empleado_service.client.SucursalClient;
import cl.duoc.empleado_service.dto.EmpleadoDTO;
import cl.duoc.empleado_service.dto.SucursalDTO;
import cl.duoc.empleado_service.mapper.EmpleadoMapper;
import cl.duoc.empleado_service.model.Empleado;
import cl.duoc.empleado_service.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private EmpleadoMapper empleadoMapper;
    @Autowired
    private SucursalClient sucursalClient;

    public List<EmpleadoDTO> findAll() {
        List<Empleado> empleados = empleadoRepository.findAll();
        return empleados.stream().map(empleado -> {
            SucursalDTO sucursal = sucursalClient.buscarPorId(empleado.getIdSucursal());
            return empleadoMapper.toDTO(empleado, sucursal);
        }).toList();
    }

    public EmpleadoDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado con ID " + id + " no encontrado."));
        SucursalDTO sucursal = sucursalClient.buscarPorId(empleado.getIdSucursal());
        return empleadoMapper.toDTO(empleado, sucursal);
    }

    public EmpleadoDTO save(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo.");
        }
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (empleado.getApellido() == null || empleado.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (empleado.getEmail() == null || empleado.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (empleado.getIdSucursal() == null) {
            throw new IllegalArgumentException("La sucursal es obligatoria.");
        }
        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        SucursalDTO sucursal = sucursalClient.buscarPorId(empleadoGuardado.getIdSucursal());
        return empleadoMapper.toDTO(empleadoGuardado, sucursal);
    }

    public EmpleadoDTO update(Long id, Empleado empleado) {
        Empleado empleadoExistente = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado con ID " + id + " no encontrado."));
        empleadoExistente.setNombre(empleado.getNombre());
        empleadoExistente.setApellido(empleado.getApellido());
        empleadoExistente.setEdad(empleado.getEdad());
        empleadoExistente.setEmail(empleado.getEmail());
        empleadoExistente.setCargo(empleado.getCargo());
        empleadoExistente.setIdSucursal(empleado.getIdSucursal());
        Empleado empleadoActualizado = empleadoRepository.save(empleadoExistente);
        SucursalDTO sucursal = sucursalClient.buscarPorId(empleadoActualizado.getIdSucursal());
        return empleadoMapper.toDTO(empleadoActualizado, sucursal);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado con ID " + id + " no encontrado.");
        }
        empleadoRepository.deleteById(id);
    }

    public List<EmpleadoDTO> findBySucursal(Long idSucursal) {
        List<Empleado> empleados = empleadoRepository.findByIdSucursal(idSucursal);
        SucursalDTO sucursal = sucursalClient.buscarPorId(idSucursal);
        return empleados.stream().map(e -> empleadoMapper.toDTO(e, sucursal)).toList();
    }

    public List<EmpleadoDTO> findByCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacío.");
        }
        List<Empleado> empleados = empleadoRepository.findByCargoContainingIgnoreCase(cargo);
        return empleados.stream().map(empleado -> {
            SucursalDTO sucursal = sucursalClient.buscarPorId(empleado.getIdSucursal());
            return empleadoMapper.toDTO(empleado, sucursal);
        }).toList();
    }
    public List<EmpleadoDTO> findByEdadDesde(Integer edadMinima) {
        if (edadMinima == null || edadMinima < 18) {
            throw new IllegalArgumentException("La edad minima debe ser al menos 18.");
        }
        List<Empleado> empleados = empleadoRepository.findByEdadGreaterThanEqual(edadMinima);
        return empleados.stream().map(empleado -> {
            SucursalDTO sucursal = sucursalClient.buscarPorId(empleado.getIdSucursal());
            return empleadoMapper.toDTO(empleado, sucursal);
        }).toList();
    }

    public List<EmpleadoDTO> findByDominioEmail(String dominio) {
        if (dominio == null || dominio.isBlank()) {
            throw new IllegalArgumentException("El dominio no puede estar vacio.");
        }
        String dominioNormalizado = dominio.startsWith("@") ? dominio : "@" + dominio;
        List<Empleado> empleados = empleadoRepository.findByEmailEndingWithIgnoreCase(dominioNormalizado);
        return empleados.stream().map(empleado -> {
            SucursalDTO sucursal = sucursalClient.buscarPorId(empleado.getIdSucursal());
            return empleadoMapper.toDTO(empleado, sucursal);
        }).toList();
    }

    public Long count() {
        return empleadoRepository.count();
    }
}
