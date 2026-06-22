package cl.bibliotech.clientes_service.repository;

import cl.bibliotech.clientes_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Cliente> findByEmailIgnoreCase(String email);
    List<Cliente> findByEmailEndingWithIgnoreCase(String dominio);
}
