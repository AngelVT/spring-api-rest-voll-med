package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class CancelacionAnticipacion implements ValidadorCancelamiento {

    private final ConsultaRepository consultaRepository;

    @Autowired
    public CancelacionAnticipacion(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DatosCancelamientoConsulta datos) {
        LocalDateTime fechaDeCancelacion = LocalDateTime.now();

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());

        long difference = ChronoUnit.HOURS.between(fechaDeCancelacion, consulta.getFecha());

        System.out.println("Validando anticipacion " + difference);
        if (difference < 24) {
            throw new ValidationException("Solo se puede cancelar con al menos 24 horas de anticipacion");
        }
    }
}
