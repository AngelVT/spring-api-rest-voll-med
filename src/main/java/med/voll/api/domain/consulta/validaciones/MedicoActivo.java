package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosConsulta;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicoActivo implements ValidadorDeConsultas {
    private final MedicoRepository medicoRepository;

    @Autowired
    public MedicoActivo(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }


    public void validar(DatosConsulta datos) {
        if(datos.idMedico() == null) {
            return;
        }
        var medicoActivo = medicoRepository.findActivoById(datos.idMedico());

        if(!medicoActivo) {
            throw new ValidationException("No se puede agendar consultas con medicos inactivos");
        }
    }
}
