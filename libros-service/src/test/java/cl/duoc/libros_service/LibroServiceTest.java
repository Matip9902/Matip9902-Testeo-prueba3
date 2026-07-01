package cl.duoc.libros_service;

import cl.duoc.libros_service.client.AutorClient;
import cl.duoc.libros_service.dto.AutorDTO;
import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.mapper.LibroMapper;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.repository.LibroRepository;
import cl.duoc.libros_service.service.LibroService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para LibroService")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private LibroMapper libroMapper;

    @Mock
    private AutorClient autorClient;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;
    private AutorDTO autorDTO;
    private LibroDTO libroDTO;

    @BeforeEach
    void setUp() {
        // Inicializamos los objetos que usaremos en las pruebas
        libro = new Libro(1L, "El Señor de los Anillos", 100L, 5L);

        autorDTO = new AutorDTO(); // Asumiendo que tiene constructor vacio, ajusta si es necesario
        autorDTO.setId(100L);
        autorDTO.setNombre("J.R.R. Tolkien");

        libroDTO = new LibroDTO();
        libroDTO.setId(1L);
        libroDTO.setTitulo("El Señor de los Anillos");
        libroDTO.setAutor(autorDTO);
        libroDTO.setStock(5L);
    }

    @Test
    @DisplayName("Listar libros: Debe retornar una lista de todos los libros con sus autores mapeados a DTO")
    void findAllDebeRetornarLibrosMapeados() {
        when(libroRepository.findAll()).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(100L)).thenReturn(autorDTO);
        when(libroMapper.toDTO(libro, autorDTO)).thenReturn(libroDTO);

        List<LibroDTO> resultado = libroService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("El Señor de los Anillos", resultado.get(0).getTitulo());
        assertEquals("J.R.R. Tolkien", resultado.get(0).getAutor().getNombre());

        verify(libroRepository).findAll();
        verify(autorClient).buscarPorId(100L);
        verify(libroMapper).toDTO(libro, autorDTO);
    }

    @Test
    @DisplayName("Buscar por ID: Debe retornar el LibroDTO correctamente cuando el ID existe")
    void findByIdDebeRetornarLibroCuandoExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(100L)).thenReturn(autorDTO);
        when(libroMapper.toDTO(libro, autorDTO)).thenReturn(libroDTO);

        LibroDTO resultado = libroService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals(5L, resultado.getStock());
        verify(libroRepository).findById(1L);
    }

    @Test
    @DisplayName("Buscar por ID: Debe lanzar IllegalArgumentException cuando el ID es inválido o negativo")
    void findByIdDebeLanzarErrorCuandoIdEsInvalido() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> libroService.findById(-1L)
        );

        assertTrue(error.getMessage().contains("positivo"));
        verify(libroRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Crear libro: Debe guardar exitosamente y retornar el DTO cuando los datos son válidos")
    void saveDebeGuardarLibroValido() {
        when(libroRepository.save(libro)).thenReturn(libro);
        when(autorClient.buscarPorId(100L)).thenReturn(autorDTO);
        when(libroMapper.toDTO(libro, autorDTO)).thenReturn(libroDTO);

        LibroDTO resultado = libroService.save(libro);

        assertEquals("El Señor de los Anillos", resultado.getTitulo());
        verify(libroRepository).save(libro);
        verify(autorClient).buscarPorId(100L);
    }

    @Test
    @DisplayName("Crear libro: Debe lanzar IllegalArgumentException cuando el título es nulo o está en blanco")
    void saveDebeLanzarErrorCuandoTituloEsNuloOBlanco() {
        libro.setTitulo("");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> libroService.save(libro)
        );

        assertTrue(error.getMessage().contains("titulo es obligatorio"));
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("Actualizar libro: Debe modificar los datos, consultar al cliente Autor y retornar el DTO actualizado")
    void updateDebeActualizarLibroCuandoExiste() {
        Libro datosActualizados = new Libro(null, "Cien anos de soledad", 100L, 8L);
        Libro libroActualizado = new Libro(1L, "Cien anos de soledad", 100L, 8L);
        LibroDTO dtoActualizado = new LibroDTO(1L, "Cien anos de soledad", autorDTO, 8L);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(libro)).thenReturn(libroActualizado);
        when(autorClient.buscarPorId(100L)).thenReturn(autorDTO);
        when(libroMapper.toDTO(libroActualizado, autorDTO)).thenReturn(dtoActualizado);

        LibroDTO resultado = libroService.update(1L, datosActualizados);

        assertEquals("Cien anos de soledad", resultado.getTitulo());
        assertEquals(8L, resultado.getStock());
        verify(libroRepository).findById(1L);
        verify(libroRepository).save(libro);
    }

    @Test
    @DisplayName("Actualizar libro: Debe lanzar RuntimeException cuando el libro a modificar no existe")
    void updateDebeLanzarErrorCuandoNoExiste() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> libroService.update(99L, libro)
        );

        assertTrue(error.getMessage().contains("no encontrado"));
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("Eliminar libro: Debe eliminar el registro correctamente a través del ID cuando existe")
    void deleteDebeEliminarLibroCuandoExiste() {
        when(libroRepository.existsById(1L)).thenReturn(true);

        libroService.delete(1L);

        verify(libroRepository).existsById(1L);
        verify(libroRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar libro: Debe lanzar RuntimeException si se intenta eliminar un libro que no existe")
    void deleteDebeLanzarErrorCuandoNoExiste() {
        when(libroRepository.existsById(1L)).thenReturn(false);

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> libroService.delete(1L)
        );

        assertTrue(error.getMessage().contains("no encontrado"));
        verify(libroRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Buscar por bajo stock: Debe retornar la lista de libros cuyo stock sea menor o igual al solicitado")
    void findBajoStockDebeRetornarListaDeLibros() {
        when(libroRepository.findByStockLessThanEqual(2L)).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(100L)).thenReturn(autorDTO);
        when(libroMapper.toDTO(libro, autorDTO)).thenReturn(libroDTO);

        List<LibroDTO> resultado = libroService.findBajoStock(2L);

        assertEquals(1, resultado.size());
        verify(libroRepository).findByStockLessThanEqual(2L);
    }

    @Test
    @DisplayName("Buscar por bajo stock: Debe lanzar IllegalArgumentException si el valor buscado es negativo")
    void findBajoStockDebeValidarMaximoNegativo() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> libroService.findBajoStock(-1L)
        );

        assertTrue(error.getMessage().contains("mayor o igual a 0"));
    }

    @Test
    @DisplayName("Contar libros: Debe retornar la cantidad total de libros registrados en la base de datos")
    void countDebeRetornarCantidadTotal() {
        when(libroRepository.count()).thenReturn(15L);

        Long resultado = libroService.count();

        assertEquals(15L, resultado);
        verify(libroRepository).count();
    }
}