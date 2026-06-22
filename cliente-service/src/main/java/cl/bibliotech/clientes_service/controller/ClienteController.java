package cl.bibliotech.clientes_service.controller;


import cl.bibliotech.clientes_service.model.Cliente;
import cl.bibliotech.clientes_service.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;



    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        if(clienteService.findById(id)==null)return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre){
        return ResponseEntity.ok(clienteService.findByNombre(nombre));
    }

    @GetMapping("/email")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email){
        return ResponseEntity.ok(clienteService.findByEmail(email));
    }

    @GetMapping("/dominio-email")
    public ResponseEntity<?> buscarPorDominioEmail(@RequestParam String dominio){
        return ResponseEntity.ok(clienteService.findByDominioEmail(dominio));
    }

    @GetMapping("/total")
    public ResponseEntity<?> contarClientes(){
        return ResponseEntity.ok(clienteService.count());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Cliente cliente){
        return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente){
        return ResponseEntity.ok(clienteService.update(id, cliente));

    }

}
