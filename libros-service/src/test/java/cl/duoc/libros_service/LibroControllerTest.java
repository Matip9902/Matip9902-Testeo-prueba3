package cl.duoc.libros_service;

import cl.duoc.libros_service.controller.LibroController;
import cl.duoc.libros_service.dto.AutorDTO;
import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.exception.GlobalExceptionHandler;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.service.LibroService;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Capa Controller de Libro")
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
    @DisplayName("Debe retornar una lista de libros con estado 200 OK")
    void listarDebeRetornarOk() throws Exception {
        when(libroService.findAll()).thenReturn(List.of(libroDTO));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Cien anos de soledad"));
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando el ID del libro no existe")
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(libroService.findById(99L)).thenThrow(new RuntimeException("Libro con ID 99 no encontrado."));

        mockMvc.perform(get("/api/v1/libros/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Debe retornar estado 400 Bad Request cuando el ID proporcionado para la búsqueda es inválido")
    void buscarPorIdDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(libroService.findById(0L))
                .thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(get("/api/v1/libros/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Debe retornar estado 201 Created y el objeto creado cuando el body para la creación es válido")
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
    @DisplayName("Debe retornar estado 400 Bad Request cuando se intenta crear un libro con campos vacíos o valores fuera de rango")
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

    @Test
    @DisplayName("Debe retornar el libro modificado y estado 200 OK cuando se actualiza con datos válidos")
    void actualizarDebeRetornarOk() throws Exception {
        when(libroService.update(anyLong(), any(Libro.class))).thenReturn(libroDTO);

        mockMvc.perform(put("/api/v1/libros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Cien anos de soledad",
                                  "idAutor": 1,
                                  "stock": 12
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Cien anos de soledad"));
    }

    @Test
    @DisplayName("Debe retornar estado 400 Bad Request cuando el ID proporcionado para actualizar es inválido")
    void actualizarDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(libroService.update(anyLong(), any(Libro.class)))
                .thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(put("/api/v1/libros/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Cien anos de soledad",
                                  "idAutor": 1,
                                  "stock": 12
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando se intenta actualizar un libro inexistente")
    void actualizarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(libroService.update(anyLong(), any(Libro.class)))
                .thenThrow(new RuntimeException("Libro con ID 99 no encontrado."));

        mockMvc.perform(put("/api/v1/libros/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Cien anos de soledad",
                                  "idAutor": 1,
                                  "stock": 12
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Debe retornar estado 204 No Content cuando el libro se elimina exitosamente")
    void eliminarDebeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/libros/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando se intenta eliminar un libro que no existe")
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new RuntimeException("Libro con ID 99 no encontrado."))
                .when(libroService).delete(99L);

        mockMvc.perform(delete("/api/v1/libros/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}