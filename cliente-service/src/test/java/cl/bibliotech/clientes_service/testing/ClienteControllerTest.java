package cl.bibliotech.clientes_service.testing;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        when(clienteService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/clientes/{id}", 99L))
                .andExpect(status().isNotFound());
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
}