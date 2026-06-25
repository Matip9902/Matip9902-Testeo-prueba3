package cl.duoc.autor_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AutorNotFoundException extends RuntimeException {

    public AutorNotFoundException(Long id) {
        super("Autor con ID " + id + " no encontrado.");
    }
}