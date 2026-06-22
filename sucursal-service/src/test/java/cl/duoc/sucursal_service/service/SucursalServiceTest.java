package cl.duoc.sucursal_service.service;

import cl.duoc.sucursal_service.dto.SucursalDTO;
import cl.duoc.sucursal_service.mapper.SucursalMapper;
import cl.duoc.sucursal_service.model.Sucursal;
import cl.duoc.sucursal_service.repository.SucursalRepository;
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
    void findByIdDebeRetornarSucursalCuandoExiste() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        SucursalDTO resultado = sucursalService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals(8, resultado.getCantidadEmpleados());
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void saveDebeGuardarSucursalValida() {
        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);
        when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        SucursalDTO resultado = sucursalService.save(sucursal);

        assertEquals("Av. Principal 123", resultado.getDireccion());
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void saveDebeLanzarErrorConCantidadEmpleadosNegativa() {
        sucursal.setCantidadEmpleados(-1);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sucursalService.save(sucursal)
        );

        assertTrue(error.getMessage().contains("empleados"));
    }

    @Test
    void findConDotacionHastaDebeValidarMaximo() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sucursalService.findConDotacionHasta(-1)
        );

        assertTrue(error.getMessage().contains("dotacion"));
    }
}
