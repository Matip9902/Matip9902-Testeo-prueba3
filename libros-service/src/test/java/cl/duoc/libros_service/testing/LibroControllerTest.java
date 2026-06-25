package cl.duoc.libros_service.testing;

import cl.duoc.libros_service.controller.LibroController;
import cl.duoc.libros_service.dto.AutorDTO;
import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.exception.GlobalExceptionHandler;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.service.LibroService;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {

    @Mock
    private LibroService libroService;

    private MockMvc mockMvc;
    private LibroDTO libroDTO;

    @BeforeEach
    void setUp() {
        LibroController controller = new LibroController();
        ReflectionTestUtils.setField(controller, "libroService", libroService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator()))
                .build();

        AutorDTO autorDTO = new AutorDTO(1L, "Gabriel", "Garcia Marquez", "Colombiana");
        libroDTO = new LibroDTO(1L, "Cien anos de soledad", autorDTO, 10L);
    }

    @Test
    void listarDebeRetornarOk() throws Exception {
        when(libroService.findAll()).thenReturn(List.of(libroDTO));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Cien anos de soledad"));
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(libroService.findById(99L)).thenThrow(new RuntimeException("Libro con ID 99 no encontrado."));

        mockMvc.perform(get("/api/v1/libros/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void buscarPorIdDebeRetornarBadRequestConIdInvalido() throws Exception {
        when(libroService.findById(0L)).thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(get("/api/v1/libros/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void crearDebeRetornarCreated() throws Exception {
        when(libroService.save(any(Libro.class))).thenReturn(libroDTO);

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Cien anos de soledad",
                                  "idAutor": 1,
                                  "stock": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Cien anos de soledad"));
    }

    @Test
    void crearDebeRetornarBadRequestConBodyInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "",
                                  "idAutor": -1,
                                  "stock": -1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}