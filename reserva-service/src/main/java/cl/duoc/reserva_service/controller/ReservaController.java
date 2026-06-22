package cl.duoc.reserva_service.controller;

import cl.duoc.reserva_service.dto.ReservaDTO;
import cl.duoc.reserva_service.model.Reserva;
import cl.duoc.reserva_service.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarTodas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody Reserva reserva) {
        return new ResponseEntity<>(reservaService.save(reserva), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(@PathVariable Long id, @Valid @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.update(id, reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaDTO>> listarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(reservaService.findByIdCliente(idCliente));
    }

    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<ReservaDTO>> listarPorLibro(@PathVariable Long idLibro) {
        return ResponseEntity.ok(reservaService.findByIdLibro(idLibro));
    }

    @GetMapping("/estado")
    public ResponseEntity<List<ReservaDTO>> listarPorEstado(@RequestParam String estado) {
        return ResponseEntity.ok(reservaService.findByEstado(estado));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<ReservaDTO>> listarActivas() {
        return ResponseEntity.ok(reservaService.findActivas());
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<ReservaDTO>> listarPorFechas(
            @RequestParam String desde,
            @RequestParam String hasta) {
        return ResponseEntity.ok(reservaService.findByFechaReservaEntre(LocalDate.parse(desde), LocalDate.parse(hasta)));
    }
}
