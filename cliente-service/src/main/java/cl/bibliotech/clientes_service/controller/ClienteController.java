package cl.bibliotech.clientes_service.controller;

import cl.bibliotech.clientes_service.dto.ClienteDTO;
import cl.bibliotech.clientes_service.model.Cliente;
import cl.bibliotech.clientes_service.service.ClienteService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones para crear, consultar, actualizar y eliminar clientes.")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna todos los clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Clientes obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": 1,
                                "nombre": "Matias",
                                "email": "matias@bibliotech.cl"
                              }
                            ]
                            """)))
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna un cliente especifico segun su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "nombre": "Matias",
                                      "email": "matias@bibliotech.cl"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID del cliente a consultar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nombre", description = "Filtra clientes cuyo nombre coincida total o parcialmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro nombre invalido", content = @Content)
    })
    public ResponseEntity<?> buscarPorNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar.", example = "Matias", required = true)
            @RequestParam @NotBlank(message = "El nombre no puede estar vacio.") String nombre) {
        return ResponseEntity.ok(clienteService.findByNombre(nombre));
    }

    @GetMapping("/email")
    @Operation(summary = "Buscar cliente por email", description = "Retorna un cliente a partir de su correo electronico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro email invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    public ResponseEntity<?> buscarPorEmail(
            @Parameter(description = "Email usado como filtro.", example = "matias@bibliotech.cl", required = true)
            @RequestParam @NotBlank(message = "El email no puede estar vacio.") String email) {
        return ResponseEntity.ok(clienteService.findByEmail(email));
    }

    @GetMapping("/dominio-email")
    @Operation(summary = "Buscar clientes por dominio de email", description = "Filtra clientes por dominio del correo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametro dominio invalido", content = @Content)
    })
    public ResponseEntity<?> buscarPorDominioEmail(
            @Parameter(description = "Dominio de correo.", example = "bibliotech.cl", required = true)
            @RequestParam @NotBlank(message = "El dominio no puede estar vacio.") String dominio) {
        return ResponseEntity.ok(clienteService.findByDominioEmail(dominio));
    }

    @GetMapping("/total")
    @Operation(summary = "Contar clientes", description = "Retorna la cantidad total de clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Total obtenido correctamente",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "15")))
    public ResponseEntity<?> contarClientes() {
        return ResponseEntity.ok(clienteService.count());
    }

    @PostMapping
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente validando nombre, email y password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos para crear el cliente", content = @Content)
    })
    public ResponseEntity<?> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cliente.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "nombre": "Matias",
                                      "email": "matias@bibliotech.cl",
                                      "password": "secreto123"
                                    }
                                    """)))
            @Valid @RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente registrado por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del cliente a eliminar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o parametro invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del cliente a actualizar.", example = "1", required = true)
            @PathVariable @Positive(message = "El ID debe ser positivo.") Long id,
            @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }
}