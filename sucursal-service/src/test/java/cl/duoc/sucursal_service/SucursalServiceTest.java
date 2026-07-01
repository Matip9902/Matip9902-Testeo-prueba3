package cl.duoc.sucursal_service;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.mapper.SucursalMapper;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.repository.SucursalRepository;
import cl.duoc.sucursal_service.service.SucursalService;
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
@DisplayName("Pruebas Unitarias para SucursalService")
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private SucursalMapper sucursalMapper;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursal;
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal(1L, "Santiago", "Av. Principal 123", 8);
        sucursalDTO = new SucursalDTO();
        sucursalDTO.setId(1L);
        sucursalDTO.setComuna("Santiago");
        sucursalDTO.setDireccion("Av. Principal 123");
        sucursalDTO.setCantidadEmpleados(8);
    }

    @Test
    @DisplayName("Listar sucursales: Debe retornar una lista de todas las sucursales convertidas a DTO")
    void findAllDebeRetornarSucursalesMapeadas() {
        when(sucursalRepository.findAll()).thenReturn(List.of(sucursal));
        when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        List<SucursalDTO> resultado = sucursalService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getComuna());
        verify(sucursalRepository).findAll();
        verify(sucursalMapper).toDTO(sucursal);
    }

    @Test
    @DisplayName("Buscar por ID: Debe retornar el SucursalDTO correspondiente cuando la sucursal existe")
    void findByIdDebeRetornarSucursalCuandoExiste() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        SucursalDTO resultado = sucursalService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals(8, resultado.getCantidadEmpleados());
        verify(sucursalRepository).findById(1L);
    }

    @Test
    @DisplayName("Crear sucursal: Debe guardar exitosamente y retornar el DTO cuando los datos son válidos")
    void saveDebeGuardarSucursalValida() {
        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);
        when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        SucursalDTO resultado = sucursalService.save(sucursal);

        assertEquals("Av. Principal 123", resultado.getDireccion());
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    @DisplayName("Crear sucursal: Debe lanzar IllegalArgumentException si la cantidad de empleados es negativa")
    void saveDebeLanzarErrorConCantidadEmpleadosNegativa() {
        sucursal.setCantidadEmpleados(-1);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sucursalService.save(sucursal)
        );

        assertTrue(error.getMessage().contains("empleados"));
    }

    @Test
    @DisplayName("Actualizar sucursal: Debe modificar los datos, guardar en el repositorio y retornar el DTO actualizado")
    void updateDebeActualizarSucursalCuandoExiste() {
        Sucursal datosActualizados = new Sucursal(null, "Providencia", "Av. Nueva 123", 12);
        Sucursal sucursalActualizada = new Sucursal(1L, "Providencia", "Av. Nueva 123", 12);
        SucursalDTO dtoActualizado = new SucursalDTO();
        dtoActualizado.setId(1L);
        dtoActualizado.setComuna("Providencia");
        dtoActualizado.setDireccion("Av. Nueva 123");
        dtoActualizado.setCantidadEmpleados(12);

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(sucursal)).thenReturn(sucursalActualizada);
        when(sucursalMapper.toDTO(sucursalActualizada)).thenReturn(dtoActualizado);

        SucursalDTO resultado = sucursalService.update(1L, datosActualizados);

        assertEquals("Providencia", resultado.getComuna());
        assertEquals(12, resultado.getCantidadEmpleados());
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    @DisplayName("Actualizar sucursal: Debe lanzar RuntimeException cuando la sucursal a modificar no existe")
    void updateDebeLanzarErrorCuandoNoExiste() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> sucursalService.update(99L, sucursal)
        );

        assertTrue(error.getMessage().contains("no encontrada"));
        verify(sucursalRepository, never()).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Actualizar sucursal: Debe lanzar IllegalArgumentException si el ID proporcionado es inválido o negativo")
    void updateDebeValidarIdInvalido() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sucursalService.update(0L, sucursal)
        );

        assertTrue(error.getMessage().contains("positivo"));
        verify(sucursalRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Eliminar sucursal: Debe eliminar el registro correctamente cuando el ID de la sucursal existe")
    void deleteDebeEliminarSucursalCuandoExiste() {
        when(sucursalRepository.existsById(1L)).thenReturn(true);

        sucursalService.delete(1L);

        verify(sucursalRepository).existsById(1L);
        verify(sucursalRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar sucursal: Debe lanzar RuntimeException si se intenta eliminar una sucursal que no existe")
    void deleteDebeLanzarErrorCuandoNoExiste() {
        when(sucursalRepository.existsById(99L)).thenReturn(false);

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> sucursalService.delete(99L)
        );

        assertTrue(error.getMessage().contains("no encontrada"));
        verify(sucursalRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Buscar por dotación: Debe lanzar IllegalArgumentException si la cantidad máxima de empleados buscada es negativa")
    void findConDotacionHastaDebeValidarMaximo() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sucursalService.findConDotacionHasta(-1)
        );

        assertTrue(error.getMessage().contains("dotacion"));
    }
}