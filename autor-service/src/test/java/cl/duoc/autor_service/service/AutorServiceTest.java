package cl.duoc.autor_service.service;

import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private AutorDTO autorDTO;

    @BeforeEach
    void setUp() {
        autor = new Autor(1L, "Gabriel", "Garcia Marquez", "Colombiana");
        autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel");
        autorDTO.setApellido("Garcia Marquez");
        autorDTO.setNacionalidad("Colombiana");
    }

    @Test
    void findAllDebeRetornarAutoresMapeados() {
        when(autorRepository.findAll()).thenReturn(List.of(autor));
        when(autorMapper.toDTO(autor)).thenReturn(autorDTO);

        List<AutorDTO> resultado = autorService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Gabriel", resultado.get(0).getNombre());
        verify(autorRepository).findAll();
        verify(autorMapper).toDTO(autor);
    }

    @Test
    void findByIdDebeRetornarAutorCuandoExiste() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorMapper.toDTO(autor)).thenReturn(autorDTO);

        AutorDTO resultado = autorService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Garcia Marquez", resultado.getApellido());
        verify(autorRepository).findById(1L);
    }

    @Test
    void findByIdDebeLanzarErrorConIdInvalido() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.findById(0L)
        );

        assertTrue(error.getMessage().contains("ID"));
    }

    @Test
    void saveDebeGuardarAutorValido() {
        when(autorRepository.save(autor)).thenReturn(autor);
        when(autorMapper.toDTO(autor)).thenReturn(autorDTO);

        AutorDTO resultado = autorService.save(autor);

        assertEquals("Colombiana", resultado.getNacionalidad());
        verify(autorRepository).save(autor);
    }

    @Test
    void saveDebeLanzarErrorCuandoNombreEstaVacio() {
        autor.setNombre("");

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.save(autor)
        );

        assertTrue(error.getMessage().contains("nombre"));
    }
}
