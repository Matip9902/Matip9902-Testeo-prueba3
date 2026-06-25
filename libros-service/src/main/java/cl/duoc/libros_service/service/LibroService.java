package cl.duoc.libros_service.service;

import cl.duoc.libros_service.client.AutorClient;
import cl.duoc.libros_service.dto.AutorDTO;
import cl.duoc.libros_service.dto.LibroDTO;
import cl.duoc.libros_service.mapper.LibroMapper;
import cl.duoc.libros_service.model.Libro;
import cl.duoc.libros_service.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private LibroMapper libroMapper;
    @Autowired
    private AutorClient autorClient;

    public List<LibroDTO> findAll() {
        List<Libro> libros = libroRepository.findAll();
        return libros.stream().map(libro -> {
            AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
            return libroMapper.toDTO(libro, autor);
        }).toList();
    }

    public LibroDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro con ID " + id + " no encontrado."));
        AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
        return libroMapper.toDTO(libro, autor);
    }

    public LibroDTO save(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (libro.getIdAutor() == null) {
            throw new IllegalArgumentException("El autor es obligatorio.");
        }
        Libro libroGuardado = libroRepository.save(libro);
        AutorDTO autor = autorClient.buscarPorId(libroGuardado.getIdAutor());
        return libroMapper.toDTO(libroGuardado, autor);
    }

    public LibroDTO update(Long id, Libro libro) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (libro.getIdAutor() == null) {
            throw new IllegalArgumentException("El autor es obligatorio.");
        }

        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro con ID " + id + " no encontrado."));
        libroExistente.setTitulo(libro.getTitulo());
        libroExistente.setIdAutor(libro.getIdAutor());
        libroExistente.setStock(libro.getStock());

        Libro libroActualizado = libroRepository.save(libroExistente);
        AutorDTO autor = autorClient.buscarPorId(libroActualizado.getIdAutor());
        return libroMapper.toDTO(libroActualizado, autor);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro con ID " + id + " no encontrado.");
        }
        libroRepository.deleteById(id);
    }

    public List<LibroDTO> findByTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);
        return libros.stream().map(libro -> {
            AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
            return libroMapper.toDTO(libro, autor);
        }).toList();
    }

    public Long count() {
        return libroRepository.count();
    }

    public List<LibroDTO> findDisponibles() {
        List<Libro> libros = libroRepository.findByStockGreaterThan(0L);
        return libros.stream().map(libro -> {
            AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
            return libroMapper.toDTO(libro, autor);
        }).toList();
    }
    public List<LibroDTO> findByAutor(Long idAutor) {
        if (idAutor == null || idAutor <= 0) {
            throw new IllegalArgumentException("El ID del autor debe ser positivo.");
        }
        List<Libro> libros = libroRepository.findByIdAutor(idAutor);
        AutorDTO autor = autorClient.buscarPorId(idAutor);
        return libros.stream().map(libro -> libroMapper.toDTO(libro, autor)).toList();
    }

    public List<LibroDTO> findSinStock() {
        List<Libro> libros = libroRepository.findByStock(0L);
        return libros.stream().map(libro -> {
            AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
            return libroMapper.toDTO(libro, autor);
        }).toList();
    }

    public List<LibroDTO> findBajoStock(Long maximo) {
        if (maximo == null || maximo < 0) {
            throw new IllegalArgumentException("El stock maximo debe ser mayor o igual a 0.");
        }
        List<Libro> libros = libroRepository.findByStockLessThanEqual(maximo);
        return libros.stream().map(libro -> {
            AutorDTO autor = autorClient.buscarPorId(libro.getIdAutor());
            return libroMapper.toDTO(libro, autor);
        }).toList();
    }
}
