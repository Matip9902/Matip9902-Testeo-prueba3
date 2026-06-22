package cl.duoc.autor_service.service;


import cl.duoc.autor_service.dto.AutorDTO;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private AutorMapper autorMapper;

    public List<AutorDTO> findAll() {
        List<Autor> autores = autorRepository.findAll();
        return autores.stream().map(autorMapper::toDTO).toList();
    }

    public AutorDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor con ID " + id + " no encontrado."));
        return autorMapper.toDTO(autor);
    }

    public AutorDTO save(Autor autor) {
        if (autor == null) {
            throw new IllegalArgumentException("El autor no puede ser nulo.");
        }
        if (autor.getNombre() == null || autor.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (autor.getApellido() == null || autor.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        return autorMapper.toDTO(autorRepository.save(autor));
    }

    public AutorDTO update(Long id, Autor autor) {
        Autor autorExistente = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor con ID " + id + " no encontrado."));
        autorExistente.setNombre(autor.getNombre());
        autorExistente.setApellido(autor.getApellido());
        autorExistente.setNacionalidad(autor.getNacionalidad());
        return autorMapper.toDTO(autorRepository.save(autorExistente));
    }

    public void delete(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new RuntimeException("Autor con ID " + id + " no encontrado.");
        }
        autorRepository.deleteById(id);
    }

    public List<AutorDTO> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío.");
        }
        List<Autor> autores = autorRepository.findByNombreContainingIgnoreCase(nombre);
        return autores.stream().map(autorMapper::toDTO).toList();
    }

    public List<AutorDTO> findByNacionalidad(String nacionalidad) {
        if (nacionalidad == null || nacionalidad.isBlank()) {
            throw new IllegalArgumentException("La nacionalidad no puede estar vacía.");
        }
        List<Autor> autores = autorRepository.findByNacionalidad(nacionalidad);
        return autores.stream().map(autorMapper::toDTO).toList();
    }


    public List<AutorDTO> findByApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio.");
        }
        List<Autor> autores = autorRepository.findByApellidoContainingIgnoreCase(apellido);
        return autores.stream().map(autorMapper::toDTO).toList();
    }

    public Long count() {
        return autorRepository.count();
    }
}
