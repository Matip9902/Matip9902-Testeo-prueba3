package cl.bibliotech.clientes_service.mapper;

import cl.bibliotech.clientes_service.dto.ClienteDTO;
import cl.bibliotech.clientes_service.model.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteMapper {
    public ClienteDTO toDTO(Cliente cliente) {
        //Validar
        if(cliente == null) return null;

        //creamos el objeto dto vacio
        ClienteDTO dto = new ClienteDTO();

        //darle valores
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        return dto;

    }


    public List<ClienteDTO> toListDTO(List<Cliente> listado) {
        return listado.stream()
                .map(this::toDTO)
                .toList();
    }
}
