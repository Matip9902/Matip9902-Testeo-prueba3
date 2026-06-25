package cl.duoc.sucursal_service.service;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.mapper.SucursalMapper;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;
    @Autowired
    private SucursalMapper sucursalMapper;

    public List<SucursalDTO> findAll() {
        List<Sucursal> sucursales = sucursalRepository.findAll();
        return sucursales.stream().map(sucursalMapper::toDTO).toList();
    }

    public SucursalDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal con ID " + id + " no encontrada."));
        return sucursalMapper.toDTO(sucursal);
    }

    public SucursalDTO save(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula.");
        }
        if (sucursal.getComuna() == null || sucursal.getComuna().isBlank()) {
            throw new IllegalArgumentException("La comuna es obligatoria.");
        }
        if (sucursal.getDireccion() == null || sucursal.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        if (sucursal.getCantidadEmpleados() == null || sucursal.getCantidadEmpleados() < 0) {
            throw new IllegalArgumentException("La cantidad de empleados debe ser mayor o igual a 0.");
        }
        return sucursalMapper.toDTO(sucursalRepository.save(sucursal));
    }

    public SucursalDTO update(Long id, Sucursal sucursal) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula.");
        }
        if (sucursal.getComuna() == null || sucursal.getComuna().isBlank()) {
            throw new IllegalArgumentException("La comuna es obligatoria.");
        }
        if (sucursal.getDireccion() == null || sucursal.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La direccion es obligatoria.");
        }
        if (sucursal.getCantidadEmpleados() == null || sucursal.getCantidadEmpleados() < 0) {
            throw new IllegalArgumentException("La cantidad de empleados debe ser mayor o igual a 0.");
        }

        Sucursal sucursalExistente = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal con ID " + id + " no encontrada."));
        sucursalExistente.setComuna(sucursal.getComuna());
        sucursalExistente.setDireccion(sucursal.getDireccion());
        sucursalExistente.setCantidadEmpleados(sucursal.getCantidadEmpleados());
        return sucursalMapper.toDTO(sucursalRepository.save(sucursalExistente));
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        if (!sucursalRepository.existsById(id)) {
            throw new RuntimeException("Sucursal con ID " + id + " no encontrada.");
        }
        sucursalRepository.deleteById(id);
    }

    public List<SucursalDTO> findByComuna(String comuna) {
        if (comuna == null || comuna.isBlank()) {
            throw new IllegalArgumentException("La comuna no puede estar vacía.");
        }
        List<Sucursal> sucursales = sucursalRepository.findByComunaContainingIgnoreCase(comuna);
        return sucursales.stream().map(sucursalMapper::toDTO).toList();
    }
    public List<SucursalDTO> findConEmpleados() {
        List<Sucursal> sucursales = sucursalRepository.findByCantidadEmpleadosGreaterThan(0);
        return sucursales.stream().map(sucursalMapper::toDTO).toList();
    }

    public List<SucursalDTO> findSinEmpleados() {
        List<Sucursal> sucursales = sucursalRepository.findByCantidadEmpleados(0);
        return sucursales.stream().map(sucursalMapper::toDTO).toList();
    }

    public List<SucursalDTO> findConDotacionHasta(Integer maximo) {
        if (maximo == null || maximo < 0) {
            throw new IllegalArgumentException("La dotacion maxima debe ser mayor o igual a 0.");
        }
        List<Sucursal> sucursales = sucursalRepository.findByCantidadEmpleadosLessThanEqual(maximo);
        return sucursales.stream().map(sucursalMapper::toDTO).toList();
    }

    public Long count() {
        return sucursalRepository.count();
    }
}
