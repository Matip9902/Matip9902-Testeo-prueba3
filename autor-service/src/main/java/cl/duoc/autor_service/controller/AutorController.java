package cl.duoc.autor_service.controller;

import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.service.AutorService;
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

// Documentacion Swagger trabajada por Matias imil.
@Validated
@RestController
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "Operaciones para crear, consultar, actualizar y eliminar autores.")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar autores", description = "Retorna todos los autores registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autores obtenidos correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorDTO.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "nombre": "Gabriel",
                                        "apellido": "Garcia Marquez",
                                        "nacionalidad": "Colombiana"
                                      }
                                    ]
                                    """)))
    })
    public ResponseEntity<List<AutorDTO>> listar() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Retorna un autor especifico segun su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "nombre": "Gabriel",
                                      "apellido": "Garcia Marquez",
                                      "nacionalidad": "Colombiana"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AutorDTO.class),
                    examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "nombre": "Gabriel",
                                        "apellido": "Garcia Marquez",
                                        "nacionalidad": "Colombiana"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    public ResponseEntity<AutorDTO> buscarPorId(
            @Parameter(description = "ID del autor a consultar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear autor", description = "Registra un nuevo autor validando los datos obligatorios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 16,
                                      "nombre": "Gabriel",
                                      "apellido": "Garcia Marquez",
                                      "nacionalidad": "Colombiana"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos para crear el autor", content = @Content)
    })
    public ResponseEntity<AutorDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del autor a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "nombre": "Gabriel",
                                      "apellido": "Garcia Marquez",
                                      "nacionalidad": "Colombiana"
                                    }
                                    """)))
            @Valid @RequestBody Autor autor) {
        return ResponseEntity.status(201).body(autorService.save(autor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor", description = "Actualiza los datos de un autor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    public ResponseEntity<AutorDTO> actualizar(
            @Parameter(description = "ID del autor a actualizar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del autor.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "nombre": "Gabriel",
                                      "apellido": "Garcia Marquez",
                                      "nacionalidad": "Colombiana"
                                    }
                                    """)))
            @Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.update(id, autor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor", description = "Elimina un autor registrado por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del autor a eliminar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar autores por nombre", description = "Filtra autores cuyo nombre coincida total o parcialmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro nombre invalido", content = @Content)
    })
    public ResponseEntity<List<AutorDTO>> buscarPorNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar.", example = "Gabriel", required = true)
            @RequestParam @NotBlank(message = "El nombre no puede estar vacio.") String nombre) {
        return ResponseEntity.ok(autorService.findByNombre(nombre));
    }

    @GetMapping("/nacionalidad")
    @Operation(summary = "Buscar autores por nacionalidad", description = "Retorna autores filtrados por nacionalidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro nacionalidad invalido", content = @Content)
    })
    public ResponseEntity<List<AutorDTO>> buscarPorNacionalidad(
            @Parameter(description = "Nacionalidad usada como filtro.", example = "Chilena", required = true)
            @RequestParam @NotBlank(message = "La nacionalidad no puede estar vacia.") String nacionalidad) {
        return ResponseEntity.ok(autorService.findByNacionalidad(nacionalidad));
    }

    @GetMapping("/apellido")
    @Operation(summary = "Buscar autores por apellido", description = "Filtra autores cuyo apellido coincida total o parcialmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro apellido invalido", content = @Content)
    })
    public ResponseEntity<List<AutorDTO>> buscarPorApellido(
            @Parameter(description = "Apellido o parte del apellido a buscar.", example = "Garcia", required = true)
            @RequestParam @NotBlank(message = "El apellido no puede estar vacio.") String apellido) {
        return ResponseEntity.ok(autorService.findByApellido(apellido));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar autores", description = "Retorna la cantidad total de autores registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total de autores obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "15")))
    })
    public ResponseEntity<Long> contarAutores() {
        return ResponseEntity.ok(autorService.count());
    }
}
