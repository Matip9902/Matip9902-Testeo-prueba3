package cl.duoc.autor_service.controller;

import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import cl.duoc.autor_service.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "Operaciones para crear, consultar, actualizar y eliminar autores.")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar autores", description = "Retorna todos los autores registrados.")
    public ResponseEntity<List<AutorDTO>> listar() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Retorna un autor especifico segun su identificador.")
    public ResponseEntity<AutorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear autor", description = "Registra un nuevo autor validando los datos obligatorios.")
    public ResponseEntity<AutorDTO> crear(@Valid @RequestBody Autor autor) {
        return ResponseEntity.status(201).body(autorService.save(autor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor", description = "Actualiza los datos de un autor existente.")
    public ResponseEntity<AutorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.update(id, autor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor", description = "Elimina un autor registrado por su identificador.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar autores por nombre", description = "Filtra autores cuyo nombre coincida total o parcialmente.")
    public ResponseEntity<List<AutorDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(autorService.findByNombre(nombre));
    }

    @GetMapping("/nacionalidad")
    @Operation(summary = "Buscar autores por nacionalidad", description = "Retorna autores filtrados por nacionalidad.")
    public ResponseEntity<List<AutorDTO>> buscarPorNacionalidad(@RequestParam String nacionalidad) {
        return ResponseEntity.ok(autorService.findByNacionalidad(nacionalidad));
    }

    @GetMapping("/apellido")
    @Operation(summary = "Buscar autores por apellido", description = "Filtra autores cuyo apellido coincida total o parcialmente.")
    public ResponseEntity<List<AutorDTO>> buscarPorApellido(@RequestParam String apellido) {
        return ResponseEntity.ok(autorService.findByApellido(apellido));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar autores", description = "Retorna la cantidad total de autores registrados.")
    public ResponseEntity<Long> contarAutores() {
        return ResponseEntity.ok(autorService.count());
    }
}
