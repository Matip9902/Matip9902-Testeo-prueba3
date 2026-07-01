package cl.bibliotech.clientes_service;

import cl.bibliotech.clientes_service.dto.ClienteDTO;
import cl.bibliotech.clientes_service.mapper.ClienteMapper;
import cl.bibliotech.clientes_service.model.Cliente;
import cl.bibliotech.clientes_service.repository.ClienteRepository;
import cl.bibliotech.clientes_service.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Juan Perez", "juan@gmail.com", "secreta123");

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan Perez");
    }

    @Test
    @DisplayName("Listar clientes: Debe retornar una lista de todos los clientes")
    void findAllDebeRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Buscar por ID: Debe retornar el ClienteDTO cuando el ID existe")
    void findByIdDebeRetornarClienteDTOCuandoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Crear cliente: Debe guardar exitosamente y retornar la entidad Cliente")
    void saveDebeGuardarYRetornarCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.save(cliente);

        assertEquals("Juan Perez", resultado.getNombre());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Crear cliente: Debe lanzar IllegalArgumentException cuando el nombre está vacío")
    void saveDebeValidarNombreObligatorio() {
        cliente.setNombre("");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.save(cliente)
        );

        assertTrue(error.getMessage().contains("nombre"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Eliminar cliente: Debe llamar al repositorio correctamente para eliminar por ID")
    void deleteDebeLlamarAlRepositorio() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.delete(1L);

        verify(clienteRepository).existsById(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Actualizar cliente: Debe modificar los datos y guardar cuando el cliente existe")
    void updateDebeActualizarDatosCuandoClienteExiste() {
        Cliente datosNuevos = new Cliente(null, "Juan Modificado", "nuevo@gmail.com", "nuevaPass");
        Cliente clienteGuardado = new Cliente(1L, "Juan Modificado", "nuevo@gmail.com", "nuevaPass");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.update(1L, datosNuevos);

        assertEquals("Juan Modificado", resultado.getNombre());
        assertEquals("nuevo@gmail.com", resultado.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Actualizar cliente: Debe lanzar IllegalArgumentException si el ID es inválido (ej. 0 o negativo)")
    void updateDebeValidarIdInvalido() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.update(0L, cliente)
        );

        assertTrue(error.getMessage().contains("positivo"));
        verify(clienteRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Actualizar cliente: Debe lanzar RuntimeException cuando el cliente a modificar no existe")
    void updateDebeLanzarErrorCuandoNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> clienteService.update(99L, cliente)
        );

        assertTrue(error.getMessage().contains("no encontrado"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Buscar por nombre: Debe retornar una lista de ClienteDTO si hay coincidencias")
    void findByNombreDebeRetornarListaDTO() {
        when(clienteRepository.findByNombreContainingIgnoreCase("Juan")).thenReturn(List.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        List<ClienteDTO> resultado = clienteService.findByNombre("Juan");

        assertEquals(1, resultado.size());
        verify(clienteRepository).findByNombreContainingIgnoreCase("Juan");
    }

    @Test
    @DisplayName("Buscar por nombre: Debe lanzar IllegalArgumentException si el parámetro de búsqueda está vacío")
    void findByNombreDebeLanzarErrorSiVacio() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.findByNombre("")
        );
        assertTrue(error.getMessage().contains("vacio"));
    }

    @Test
    @DisplayName("Buscar por email: Debe retornar el ClienteDTO correspondiente al correo exacto")
    void findByEmailDebeRetornarDTO() {
        when(clienteRepository.findByEmailIgnoreCase("juan@gmail.com")).thenReturn(Optional.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.findByEmail("juan@gmail.com");

        assertEquals("Juan Perez", resultado.getNombre());
    }

    @Test
    @DisplayName("Buscar por email: Debe lanzar RuntimeException si el correo no está registrado")
    void findByEmailDebeLanzarErrorSiNoExiste() {
        when(clienteRepository.findByEmailIgnoreCase("noexiste@gmail.com")).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> clienteService.findByEmail("noexiste@gmail.com")
        );
        assertTrue(error.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Buscar por dominio: Debe formatear correctamente la búsqueda y retornar la lista de ClienteDTO")
    void findByDominioEmailDebeNormalizarYRetornar() {
        when(clienteRepository.findByEmailEndingWithIgnoreCase("@gmail.com")).thenReturn(List.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        List<ClienteDTO> resultado = clienteService.findByDominioEmail("gmail.com");

        assertEquals(1, resultado.size());
        verify(clienteRepository).findByEmailEndingWithIgnoreCase("@gmail.com");
    }

    @Test
    @DisplayName("Contar clientes: Debe retornar la cantidad total de registros en la base de datos")
    void countDebeRetornarTotal() {
        when(clienteRepository.count()).thenReturn(42L);

        Long resultado = clienteService.count();

        assertEquals(42L, resultado);
        verify(clienteRepository).count();
    }
}