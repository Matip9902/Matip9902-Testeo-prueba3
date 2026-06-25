package cl.bibliotech.clientes_service;

import cl.bibliotech.clientes_service.controller.ClienteController;
import cl.bibliotech.clientes_service.dto.ClienteDTO;
import cl.bibliotech.clientes_service.exception.GlobalExceptionHandler;
import cl.bibliotech.clientes_service.model.Cliente;
import cl.bibliotech.clientes_service.service.ClienteService;
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
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;
    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        ClienteController controller = new ClienteController();
        ReflectionTestUtils.setField(controller, "clienteService", clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator()))
                .build();

        cliente = new Cliente(1L, "Matias", "matias@bibliotech.cl", "secreto123");
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Matias");
    }

    @Test
    void listarDebeRetornarOk() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matias"));
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(clienteService.findById(99L)).thenThrow(new RuntimeException("Cliente con ID 99 no encontrado."));

        mockMvc.perform(get("/api/v1/clientes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void buscarPorIdDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(clienteService.findById(0L))
                .thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(get("/api/v1/clientes/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void crearDebeRetornarCreated() throws Exception {
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Matias",
                                  "email": "matias@bibliotech.cl",
                                  "password": "secreto123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("matias@bibliotech.cl"));
    }

    @Test
    void crearDebeRetornarBadRequestConBodyInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "email": "correo-invalido",
                                  "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarDebeRetornarOk() throws Exception {
        when(clienteService.update(anyLong(), any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/v1/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Matias",
                                  "email": "matias@bibliotech.cl",
                                  "password": "secreto123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("matias@bibliotech.cl"));
    }

    @Test
    void actualizarDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(clienteService.update(anyLong(), any(Cliente.class)))
                .thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(put("/api/v1/clientes/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Matias",
                                  "email": "matias@bibliotech.cl",
                                  "password": "secreto123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void actualizarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(clienteService.update(anyLong(), any(Cliente.class)))
                .thenThrow(new RuntimeException("Cliente con ID 99 no encontrado."));

        mockMvc.perform(put("/api/v1/clientes/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Matias",
                                  "email": "matias@bibliotech.cl",
                                  "password": "secreto123"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void eliminarDebeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/clientes/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new RuntimeException("Cliente con ID 99 no encontrado."))
                .when(clienteService).delete(99L);

        mockMvc.perform(delete("/api/v1/clientes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
