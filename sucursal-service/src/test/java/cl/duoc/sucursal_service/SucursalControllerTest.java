package cl.duoc.sucursal_service;

import cl.duoc.sucursal_service.controller.SucursalController;
import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.exception.GlobalExceptionHandler;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.service.SucursalService;
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
@DisplayName("Pruebas Unitarias - Capa Controller de Sucursal")
class SucursalControllerTest {

    @Mock
    private SucursalService sucursalService;

    private MockMvc mockMvc;
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        SucursalController controller = new SucursalController();
        ReflectionTestUtils.setField(controller, "sucursalService", sucursalService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator()))
                .build();

        sucursalDTO = new SucursalDTO();
        sucursalDTO.setId(1L);
        sucursalDTO.setComuna("Santiago");
        sucursalDTO.setDireccion("Av. Libertador 1234");
        sucursalDTO.setCantidadEmpleados(5);
    }

    @Test
    @DisplayName("Debe retornar una lista de sucursales con estado 200 OK")
    void listarDebeRetornarOk() throws Exception {
        when(sucursalService.findAll()).thenReturn(List.of(sucursalDTO));

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comuna").value("Santiago"));
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando el ID de la sucursal no existe")
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(sucursalService.findById(99L)).thenThrow(new RuntimeException("Sucursal con ID 99 no encontrada."));

        mockMvc.perform(get("/api/v1/sucursales/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Debe retornar estado 400 Bad Request cuando el ID proporcionado para la búsqueda es inválido")
    void buscarPorIdDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(sucursalService.findById(0L)).thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(get("/api/v1/sucursales/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Debe retornar estado 201 Created y el objeto creado cuando el body para la creación es válido")
    void crearDebeRetornarCreated() throws Exception {
        when(sucursalService.save(any(Sucursal.class))).thenReturn(sucursalDTO);

        mockMvc.perform(post("/api/v1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comuna": "Santiago",
                                  "direccion": "Av. Libertador 1234",
                                  "cantidadEmpleados": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comuna").value("Santiago"));
    }

    @Test
    @DisplayName("Debe retornar estado 400 Bad Request cuando se intenta crear una sucursal con campos vacíos o fuera de rango")
    void crearDebeRetornarBadRequestConBodyInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comuna": "",
                                  "direccion": "",
                                  "cantidadEmpleados": -1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar la sucursal modificada y estado 200 OK cuando se actualiza con datos válidos")
    void actualizarDebeRetornarOk() throws Exception {
        when(sucursalService.update(anyLong(), any(Sucursal.class))).thenReturn(sucursalDTO);

        mockMvc.perform(put("/api/v1/sucursales/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comuna": "Santiago",
                                  "direccion": "Av. Libertador 1234",
                                  "cantidadEmpleados": 5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comuna").value("Santiago"));
    }

    @Test
    @DisplayName("Debe retornar estado 400 Bad Request cuando el ID proporcionado para actualizar es inválido")
    void actualizarDebeRetornarBadRequestConIdInvalido() throws Exception {
        lenient().when(sucursalService.update(anyLong(), any(Sucursal.class)))
                .thenThrow(new IllegalArgumentException("El ID debe ser un numero positivo."));

        mockMvc.perform(put("/api/v1/sucursales/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comuna": "Santiago",
                                  "direccion": "Av. Libertador 1234",
                                  "cantidadEmpleados": 5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando se intenta actualizar una sucursal inexistente")
    void actualizarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(sucursalService.update(anyLong(), any(Sucursal.class)))
                .thenThrow(new RuntimeException("Sucursal con ID 99 no encontrada."));

        mockMvc.perform(put("/api/v1/sucursales/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comuna": "Santiago",
                                  "direccion": "Av. Libertador 1234",
                                  "cantidadEmpleados": 5
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Debe retornar estado 204 No Content cuando la sucursal se elimina exitosamente")
    void eliminarDebeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/sucursales/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar estado 404 Not Found cuando se intenta eliminar una sucursal que no existe")
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new RuntimeException("Sucursal con ID 99 no encontrada."))
                .when(sucursalService).delete(99L);

        mockMvc.perform(delete("/api/v1/sucursales/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}