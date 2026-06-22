package cl.duoc.empleado_service.controller;

import cl.duoc.empleado_service.dto.EmpleadoDTO;
import cl.duoc.empleado_service.model.Empleado;
import cl.duoc.empleado_service.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> listar() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> crear(@Valid @RequestBody Empleado empleado) {
        return ResponseEntity.status(201).body(empleadoService.save(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.update(id, empleado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<EmpleadoDTO>> buscarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(empleadoService.findBySucursal(idSucursal));
    }

    @GetMapping("/cargo")
    public ResponseEntity<List<EmpleadoDTO>> buscarPorCargo(@RequestParam String cargo) {
        return ResponseEntity.ok(empleadoService.findByCargo(cargo));
    }

    @GetMapping("/edad-desde")
    public ResponseEntity<List<EmpleadoDTO>> buscarPorEdadMinima(@RequestParam Integer edadMinima) {
        return ResponseEntity.ok(empleadoService.findByEdadDesde(edadMinima));
    }

    @GetMapping("/dominio-email")
    public ResponseEntity<List<EmpleadoDTO>> buscarPorDominioEmail(@RequestParam String dominio) {
        return ResponseEntity.ok(empleadoService.findByDominioEmail(dominio));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> contarEmpleados() {
        return ResponseEntity.ok(empleadoService.count());
    }
}
