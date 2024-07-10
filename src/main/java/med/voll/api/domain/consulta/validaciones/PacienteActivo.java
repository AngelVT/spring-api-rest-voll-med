package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacienteActivo implements ValidadorDeConsultas{
    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteActivo(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }


    public void validar(DatosRespuestaConsulta datos) {
        if(datos.idPaciente() == null) {
            return;
        }
        var pacienteActivo = pacienteRepository.findActivoById(datos.idPaciente());

        if(!pacienteActivo) {
            throw new ValidationException("No se puede agendar consultas con pacientes inactivos");
        }
    }
}
