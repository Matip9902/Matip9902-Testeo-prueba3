package cl.bibliotech.prestamos_service.controller;

import cl.bibliotech.prestamos_service.model.Prestamo;
import cl.bibliotech.prestamos_service.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {
    @Autowired
    private PrestamoService prestamoService;
    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(prestamoService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Object resultado = prestamoService.findById(id);
        if(resultado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos(){
        return ResponseEntity.ok(prestamoService.findActivos());
    }
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> listarPorCliente(@PathVariable Long idCliente){
        return ResponseEntity.ok(prestamoService.findByCliente(idCliente));
    }
    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<?> listarPorLibro(@PathVariable Long idLibro){
        return ResponseEntity.ok(prestamoService.findByLibro(idLibro));
    }
    @GetMapping("/atrasados")
    public ResponseEntity<?> listarAtrasados(){
        return ResponseEntity.ok(prestamoService.findAtrasados());
    }
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Prestamo prestamo){
        return new ResponseEntity<>(prestamoService.save(prestamo), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        prestamoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Prestamo prestamo){
        return ResponseEntity.ok(prestamoService.update(id,prestamo));
    }
}
