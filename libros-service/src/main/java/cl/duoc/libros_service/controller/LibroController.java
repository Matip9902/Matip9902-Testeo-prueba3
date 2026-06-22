package cl.duoc.libros_service.controller;

import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroDTO>> listar() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LibroDTO> crear(@Valid @RequestBody Libro libro) {
        return ResponseEntity.status(201).body(libroService.save(libro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroDTO>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.findByTitulo(titulo));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroDTO>> listarDisponibles() {
        return ResponseEntity.ok(libroService.findDisponibles());
    }

    @GetMapping("/autor/{idAutor}")
    public ResponseEntity<List<LibroDTO>> listarPorAutor(@PathVariable Long idAutor) {
        return ResponseEntity.ok(libroService.findByAutor(idAutor));
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<LibroDTO>> listarSinStock() {
        return ResponseEntity.ok(libroService.findSinStock());
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<LibroDTO>> listarBajoStock(@RequestParam(defaultValue = "2") Long maximo) {
        return ResponseEntity.ok(libroService.findBajoStock(maximo));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> contarLibros() {
        return ResponseEntity.ok(libroService.count());
    }
}
