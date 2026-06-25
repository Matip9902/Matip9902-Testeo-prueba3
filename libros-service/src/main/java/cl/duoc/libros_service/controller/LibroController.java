package cl.duoc.libros_service.controller;

import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/libros")
@Tag(name = "Libros", description = "Operaciones para crear, consultar, actualizar y eliminar libros.")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    @Operation(summary = "Listar libros", description = "Retorna todos los libros registrados.")
    @ApiResponse(responseCode = "200", description = "Libros obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": 1,
                                "titulo": "Cien anos de soledad",
                                "idAutor": 1,
                                "stock": 10
                              }
                            ]
                            """)))
    public ResponseEntity<List<LibroDTO>> listar() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar libro por ID", description = "Retorna un libro especifico segun su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    public ResponseEntity<LibroDTO> buscarPorId(
            @Parameter(description = "ID del libro a consultar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear libro", description = "Registra un nuevo libro validando titulo, autor y stock.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos para crear el libro", content = @Content)
    })
    public ResponseEntity<LibroDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Libro.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "titulo": "Cien anos de soledad",
                                      "idAutor": 1,
                                      "stock": 10
                                    }
                                    """)))
            @Valid @RequestBody Libro libro) {
        return ResponseEntity.status(201).body(libroService.save(libro));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "titulo": "Cien anos de soledad",
                                      "autor": {
                                        "id": 1,
                                        "nombre": "Gabriel",
                                        "apellido": "Garcia Marquez",
                                        "nacionalidad": "Colombiana"
                                      },
                                      "stock": 12
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    public ResponseEntity<LibroDTO> actualizar(
            @Parameter(description = "ID del libro a actualizar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del libro.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Libro.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "titulo": "Cien anos de soledad",
                                      "idAutor": 1,
                                      "stock": 12
                                    }
                                    """)))
            @Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.update(id, libro));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro registrado por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del libro a eliminar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar libros por titulo", description = "Filtra libros cuyo titulo coincida total o parcialmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro titulo invalido", content = @Content)
    })
    public ResponseEntity<List<LibroDTO>> buscarPorTitulo(
            @Parameter(description = "Titulo o parte del titulo a buscar.", example = "soledad", required = true)
            @RequestParam @NotBlank(message = "El titulo no puede estar vacio.") String titulo) {
        return ResponseEntity.ok(libroService.findByTitulo(titulo));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar libros disponibles", description = "Retorna libros con stock mayor a cero.")
    @ApiResponse(responseCode = "200", description = "Libros disponibles obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class)))
    public ResponseEntity<List<LibroDTO>> listarDisponibles() {
        return ResponseEntity.ok(libroService.findDisponibles());
    }

    @GetMapping("/autor/{idAutor}")
    @Operation(summary = "Listar libros por autor", description = "Retorna libros asociados al autor indicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libros del autor obtenidos correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro idAutor invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    public ResponseEntity<List<LibroDTO>> listarPorAutor(
            @Parameter(description = "ID del autor.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID del autor debe ser positivo.") Long idAutor) {
        return ResponseEntity.ok(libroService.findByAutor(idAutor));
    }

    @GetMapping("/sin-stock")
    @Operation(summary = "Listar libros sin stock", description = "Retorna libros cuyo stock sea cero.")
    @ApiResponse(responseCode = "200", description = "Libros sin stock obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class)))
    public ResponseEntity<List<LibroDTO>> listarSinStock() {
        return ResponseEntity.ok(libroService.findSinStock());
    }

    @GetMapping("/bajo-stock")
    @Operation(summary = "Listar libros con bajo stock", description = "Retorna libros con stock menor o igual al maximo indicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtro realizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LibroDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro maximo invalido", content = @Content)
    })
    public ResponseEntity<List<LibroDTO>> listarBajoStock(
            @Parameter(description = "Stock maximo permitido.", example = "2")
            @RequestParam(defaultValue = "2") @PositiveOrZero(message = "El maximo debe ser mayor o igual a cero.") Long maximo) {
        return ResponseEntity.ok(libroService.findBajoStock(maximo));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar libros", description = "Retorna la cantidad total de libros registrados.")
    @ApiResponse(responseCode = "200", description = "Total obtenido correctamente",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "15")))
    public ResponseEntity<Long> contarLibros() {
        return ResponseEntity.ok(libroService.count());
    }
}
