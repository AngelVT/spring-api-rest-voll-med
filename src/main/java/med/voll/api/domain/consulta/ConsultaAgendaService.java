package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaAgendaService {
    private final ConsultaRepository consultaRepository;
    private  final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    private final List<ValidadorDeConsultas> validadores;

    @Autowired
    public ConsultaAgendaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository, List<ValidadorDeConsultas> validadores) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.validadores = validadores;
    }

    public void agendar(DatosConsulta datosConsulta) {

        if (pacienteRepository.findById(datosConsulta.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("Este paciente no existe");
        }

        if (datosConsulta.idMedico() != null && medicoRepository.existsById(datosConsulta.idMedico())) {
            throw new ValidacionDeIntegridad("Este medico no existe");
        }

        validadores.forEach(v -> v.validar(datosConsulta));

        var paciente = pacienteRepository.findById(datosConsulta.idPaciente()).get();

        var medico = seleccionarMedico(datosConsulta);

        var consulta = new Consulta(null,medico,paciente,datosConsulta.fecha());

        consultaRepository.save(consulta);
    }

    private Medico seleccionarMedico(DatosConsulta datosConsulta) {
        if(datosConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(datosConsulta.idMedico());
        }

        if(datosConsulta.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosConsulta.especialidad(), datosConsulta.fecha());
    }
}
