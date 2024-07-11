package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicoConConsulta implements ValidadorDeConsultas {

    private final ConsultaRepository consultaRepository;

    @Autowired
    public MedicoConConsulta(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }


    public void validar(DatosConsulta datos) {
        if(datos.idMedico() == null) {
            return;
        }

        var medicoConConsulta = consultaRepository.existsByMedicoIdAndFechaAndActivoTrue(datos.idMedico(), datos.fecha());

        var medicoEnConsulta = consultaRepository.existsFechaWithinOneHour(datos.idMedico() ,datos.fecha()) > 0;

        if(medicoConConsulta || medicoEnConsulta) {
            throw new ValidationException("Medico ya tiene una consulta en el horario requerido");
        }
    }
}
