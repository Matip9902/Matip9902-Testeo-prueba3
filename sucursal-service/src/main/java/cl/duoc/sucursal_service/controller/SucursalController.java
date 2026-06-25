package cl.duoc.sucursal_service.controller;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.service.SucursalService;
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
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursales", description = "Operaciones para crear, consultar, actualizar y eliminar sucursales.")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Retorna todas las sucursales registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "comuna": "Santiago",
                                        "direccion": "Av. Libertador 1234",
                                        "cantidadEmpleados": 5
                                      }
                                    ]
                                    """)))
    })
    public ResponseEntity<List<SucursalDTO>> listar() {
        return ResponseEntity.ok(sucursalService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Retorna una sucursal especifica segun su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "comuna": "Santiago",
                                      "direccion": "Av. Libertador 1234",
                                      "cantidadEmpleados": 5
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<SucursalDTO> buscarPorId(
            @Parameter(description = "ID de la sucursal a consultar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        return ResponseEntity.ok(sucursalService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal validando comuna, direccion y dotacion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucursal creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 12,
                                      "comuna": "Santiago",
                                      "direccion": "Av. Libertador 1234",
                                      "cantidadEmpleados": 5
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos para crear la sucursal", content = @Content)
    })
    public ResponseEntity<SucursalDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la sucursal a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "comuna": "Santiago",
                                      "direccion": "Av. Libertador 1234",
                                      "cantidadEmpleados": 5
                                    }
                                    """)))
            @Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.status(201).body(sucursalService.save(sucursal));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza los datos de una sucursal existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<SucursalDTO> actualizar(
            @Parameter(description = "ID de la sucursal a actualizar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de la sucursal.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "comuna": "Santiago",
                                      "direccion": "Av. Libertador 1234",
                                      "cantidadEmpleados": 5
                                    }
                                    """)))
            @Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(sucursalService.update(id, sucursal));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal registrada por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucursal eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la sucursal a eliminar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar sucursales por comuna", description = "Filtra sucursales por comuna.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro comuna invalido", content = @Content)
    })
    public ResponseEntity<List<SucursalDTO>> buscarPorComuna(
            @Parameter(description = "Comuna usada como filtro.", example = "Santiago", required = true)
            @RequestParam @NotBlank(message = "La comuna no puede estar vacia.") String comuna) {
        return ResponseEntity.ok(sucursalService.findByComuna(comuna));
    }

    @GetMapping("/con-empleados")
    @Operation(summary = "Listar sucursales con empleados", description = "Retorna sucursales cuya dotacion sea mayor a cero.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales con empleados obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class)))
    })
    public ResponseEntity<List<SucursalDTO>> listarConEmpleados() {
        return ResponseEntity.ok(sucursalService.findConEmpleados());
    }

    @GetMapping("/sin-empleados")
    @Operation(summary = "Listar sucursales sin empleados", description = "Retorna sucursales que no tienen empleados asignados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales sin empleados obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class)))
    })
    public ResponseEntity<List<SucursalDTO>> listarSinEmpleados() {
        return ResponseEntity.ok(sucursalService.findSinEmpleados());
    }

    @GetMapping("/dotacion-hasta")
    @Operation(summary = "Filtrar sucursales por dotacion maxima", description = "Retorna sucursales con cantidad de empleados menor o igual al maximo indicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtro realizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro maximo invalido", content = @Content)
    })
    public ResponseEntity<List<SucursalDTO>> listarPorDotacionMaxima(
            @Parameter(description = "Cantidad maxima de empleados permitida.", example = "10", required = true)
            @RequestParam @PositiveOrZero(message = "El maximo debe ser mayor o igual a cero.") Integer maximo) {
        return ResponseEntity.ok(sucursalService.findConDotacionHasta(maximo));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar sucursales", description = "Retorna la cantidad total de sucursales registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total de sucursales obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "8")))
    })
    public ResponseEntity<Long> contarSucursales() {
        return ResponseEntity.ok(sucursalService.count());
    }
}
