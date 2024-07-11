package med.voll.api.infra.errors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream()
                .map(DatosErroValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity errorDeValidacion(ValidationException e) {
        DatosErroValidacion error = new DatosErroValidacion("validacion", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ValidacionDeIntegridad.class)
    public ResponseEntity errorDeValidacionIntegridad(ValidacionDeIntegridad e) {
        DatosErroValidacion error = new DatosErroValidacion("integridad", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public  ResponseEntity errorGeneral(HttpMessageNotReadableException e) {
        DatosErroValidacion error = new DatosErroValidacion("validacion", "Informacion Invalida");
        return ResponseEntity.badRequest().body(error);
    }

    private record DatosErroValidacion(String campo, String error) {
        public DatosErroValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
