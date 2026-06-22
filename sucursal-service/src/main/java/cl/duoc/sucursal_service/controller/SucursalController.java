package cl.duoc.sucursal_service.controller;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursales", description = "Operaciones para crear, consultar, actualizar y eliminar sucursales.")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Retorna todas las sucursales registradas.")
    public ResponseEntity<List<SucursalDTO>> listar() {
        return ResponseEntity.ok(sucursalService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Retorna una sucursal especifica segun su identificador.")
    public ResponseEntity<SucursalDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal validando comuna, direccion y dotacion.")
    public ResponseEntity<SucursalDTO> crear(@Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.status(201).body(sucursalService.save(sucursal));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza los datos de una sucursal existente.")
    public ResponseEntity<SucursalDTO> actualizar(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(sucursalService.update(id, sucursal));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal registrada por su identificador.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar sucursales por comuna", description = "Filtra sucursales por comuna.")
    public ResponseEntity<List<SucursalDTO>> buscarPorComuna(@RequestParam String comuna) {
        return ResponseEntity.ok(sucursalService.findByComuna(comuna));
    }

    @GetMapping("/con-empleados")
    @Operation(summary = "Listar sucursales con empleados", description = "Retorna sucursales cuya dotacion sea mayor a cero.")
    public ResponseEntity<List<SucursalDTO>> listarConEmpleados() {
        return ResponseEntity.ok(sucursalService.findConEmpleados());
    }

    @GetMapping("/sin-empleados")
    @Operation(summary = "Listar sucursales sin empleados", description = "Retorna sucursales que no tienen empleados asignados.")
    public ResponseEntity<List<SucursalDTO>> listarSinEmpleados() {
        return ResponseEntity.ok(sucursalService.findSinEmpleados());
    }

    @GetMapping("/dotacion-hasta")
    @Operation(summary = "Filtrar sucursales por dotacion maxima", description = "Retorna sucursales con cantidad de empleados menor o igual al maximo indicado.")
    public ResponseEntity<List<SucursalDTO>> listarPorDotacionMaxima(@RequestParam Integer maximo) {
        return ResponseEntity.ok(sucursalService.findConDotacionHasta(maximo));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar sucursales", description = "Retorna la cantidad total de sucursales registradas.")
    public ResponseEntity<Long> contarSucursales() {
        return ResponseEntity.ok(sucursalService.count());
    }
}
