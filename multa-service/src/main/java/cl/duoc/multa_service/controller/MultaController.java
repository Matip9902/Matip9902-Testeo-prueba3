package cl.duoc.multa_service.controller;

import cl.duoc.multa_service.dto.MultaDTO;
import cl.duoc.multa_service.model.Multa;
import cl.duoc.multa_service.service.MultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/multas")
public class MultaController {
    @Autowired
    private MultaService multaService;

    @GetMapping
    public ResponseEntity<List<MultaDTO>> listar() {
        return ResponseEntity.ok(multaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MultaDTO> registrarMulta(@Valid @RequestBody Multa multa) {
        return ResponseEntity.status(201).body(multaService.save(multa));
    }

    @PostMapping("/generar/{idPrestamo}")
    public ResponseEntity<MultaDTO> generarMulta(
            @PathVariable Long idPrestamo,
            @RequestParam String fechaDevolucionReal) {
        return ResponseEntity.status(201)
                .body(multaService.generarMulta(idPrestamo, LocalDate.parse(fechaDevolucionReal)));
    }

    @PutMapping("/pagar/{id}")
    public ResponseEntity<MultaDTO> pagarMulta(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.pagarMulta(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<MultaDTO>> multasPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(multaService.findByCliente(idCliente));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<MultaDTO>> multasPendientes() {
        return ResponseEntity.ok(multaService.findPendientes());
    }

    @GetMapping("/pagadas")
    public ResponseEntity<List<MultaDTO>> multasPagadas() {
        return ResponseEntity.ok(multaService.findPagadas());
    }

    @GetMapping("/total-pendiente")
    public ResponseEntity<Double> totalPendiente() {
        return ResponseEntity.ok(multaService.totalPendiente());
    }

    @GetMapping("/generadas")
    public ResponseEntity<List<MultaDTO>> multasGeneradasEntre(
            @RequestParam String desde,
            @RequestParam String hasta) {
        return ResponseEntity.ok(multaService.findGeneradasEntre(LocalDate.parse(desde), LocalDate.parse(hasta)));
    }
}
