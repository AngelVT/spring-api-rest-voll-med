package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicoConConsulta implements ValidadorDeConsultas {

    private final ConsultaRepository consultaRepository;

    @Autowired
    public MedicoConConsulta(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }


    public void validar(DatosRespuestaConsulta datos) {
        if(datos.idMedico() == null) {
            return;
        }

        var medicoConConsulta = consultaRepository.existsByMedicoIdAndFecha(datos.idMedico(), datos.fecha());

        if(medicoConConsulta) {
            throw new ValidationException("Medico ya tiene una consulta en el horario requerido");
        }
    }
}
