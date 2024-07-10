package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacienteSinConsulta implements ValidadorDeConsultas{

    private final ConsultaRepository consultaRepository;

    @Autowired
    public PacienteSinConsulta(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }


    public void validar(DatosRespuestaConsulta datos) {
        var primerHorario = datos.fecha().withHour(7);
        var ultimoHorario = datos.fecha().withHour(18);

        var pacienteConConsulta = consultaRepository.existsByPacienteIdAndFechaBetween(datos.idPaciente(), primerHorario, ultimoHorario);

        if(pacienteConConsulta) {
            throw new ValidationException("No se puede agendar mas de una consulta por dia");
        }
    }
}
