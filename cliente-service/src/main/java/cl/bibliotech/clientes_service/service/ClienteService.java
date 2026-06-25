package cl.bibliotech.clientes_service.service;

import cl.bibliotech.clientes_service.dto.ClienteDTO;
import cl.bibliotech.clientes_service.mapper.ClienteMapper;
import cl.bibliotech.clientes_service.model.Cliente;
import cl.bibliotech.clientes_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    @Autowired
    private ClienteMapper mapper;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public ClienteDTO findById(Long id) {
        validarId(id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));
        return mapper.toDTO(cliente);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        validarId(id);
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente con ID " + id + " no encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente update(Long id, Cliente cliente) {
        validarId(id);
        Cliente clienteUpdate = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));
        clienteUpdate.setNombre(cliente.getNombre());
        clienteUpdate.setEmail(cliente.getEmail());
        clienteUpdate.setPassword(cliente.getPassword());
        return clienteRepository.save(clienteUpdate);

    }

    public List<ClienteDTO> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        return clienteRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ClienteDTO findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio.");
        }
        Cliente cliente = clienteRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Cliente con email " + email + " no encontrado."));
        return mapper.toDTO(cliente);
    }

    public List<ClienteDTO> findByDominioEmail(String dominio) {
        if (dominio == null || dominio.isBlank()) {
            throw new IllegalArgumentException("El dominio no puede estar vacio.");
        }
        String dominioNormalizado = dominio.startsWith("@") ? dominio : "@" + dominio;
        return clienteRepository.findByEmailEndingWithIgnoreCase(dominioNormalizado)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Long count() {
        return clienteRepository.count();
    }
    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
    }
}
