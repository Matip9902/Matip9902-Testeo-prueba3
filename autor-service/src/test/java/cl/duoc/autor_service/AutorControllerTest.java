package cl.duoc.autor_service;

import cl.duoc.autor_service.controller.AutorController;
import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.exception.GlobalExceptionHandler;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.service.AutorService;
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
class AutorControllerTest {

    @Mock
    private AutorService autorService;

    private MockMvc mockMvc;
    private AutorDTO autorDTO;

    @BeforeEach
    void setUp() {
        AutorController controller = new AutorController();
        ReflectionTestUtils.setField(controller, "autorService", autorService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator()))
                .build();

        autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel");
        autorDTO.setApellido("Garcia Marquez");
        autorDTO.setNacionalidad("Colombiana");
    }

    @Test
    void listarDebeRetornarOk() throws Exception {
        when(autorService.findAll()).thenReturn(List.of(autorDTO));

        mockMvc.perform(get("/api/v1/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gabriel"));
    }

    @Test
    void buscarPorIdDebeRetornarOk() throws Exception {
        when(autorService.findById(1L)).thenReturn(autorDTO);

        mockMvc.perform(get("/api/v1/autores/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Garcia Marquez"));
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(autorService.findById(99L)).thenThrow(new RuntimeException("Autor con ID 99 no encontrado."));

        mockMvc.perform(get("/api/v1/autores/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void buscarPorIdDebeRetornarBadRequestConIdInvalido() throws Exception {
        when(autorService.findById(0L)).thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(get("/api/v1/autores/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void crearDebeRetornarCreated() throws Exception {
        when(autorService.save(any(Autor.class))).thenReturn(autorDTO);

        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Gabriel",
                                  "apellido": "Garcia Marquez",
                                  "nacionalidad": "Colombiana"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Gabriel"));
    }

    @Test
    void crearDebeRetornarBadRequestConBodyInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "apellido": "",
                                  "nacionalidad": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
