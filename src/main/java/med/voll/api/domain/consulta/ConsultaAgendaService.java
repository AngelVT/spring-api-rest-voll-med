package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorCancelamiento;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaAgendaService {
    private final ConsultaRepository consultaRepository;
    private  final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    private final List<ValidadorDeConsultas> validadores;

    private final  List<ValidadorCancelamiento> validadorCancelamientos;

    @Autowired
    public ConsultaAgendaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository, List<ValidadorDeConsultas> validadores, List<ValidadorCancelamiento> validadorCancelamientos) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.validadores = validadores;
        this.validadorCancelamientos = validadorCancelamientos;
    }

    public DatosRespuestaConsulta agendar(DatosConsulta datosConsulta) {

        if (pacienteRepository.findById(datosConsulta.idPaciente()).isEmpty()) {
            throw new ValidacionDeIntegridad("Este paciente no existe");
        }

        if (datosConsulta.idMedico() != null && !medicoRepository.existsById(datosConsulta.idMedico())) {
            throw new ValidacionDeIntegridad("Este medico no existe");
        }

        validadores.forEach(v -> v.validar(datosConsulta));

        var paciente = pacienteRepository.findById(datosConsulta.idPaciente()).get();

        var medico = seleccionarMedico(datosConsulta);

        if (medico == null) {
            throw new ValidacionDeIntegridad("No hay medicos disponibles para este Horario y especialidad");
        }

        var consulta = new Consulta(null,medico,paciente, true, null,datosConsulta.fecha());

        consultaRepository.save(consulta);

        return new DatosRespuestaConsulta(consulta);
    }

    private Medico seleccionarMedico(DatosConsulta datosConsulta) {
        if(datosConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(datosConsulta.idMedico());
        }

        if(datosConsulta.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }

        List<Medico> medicos = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosConsulta.especialidad(), datosConsulta.fecha());

        if (medicos.isEmpty()) {
            return null;
        }

        return medicos.get(0);
    }

    public Page<DatosListadoConsulta> obtenerConsultas(Pageable paginacion) {
        return consultaRepository.findByActivoTrue(paginacion).map(DatosListadoConsulta::new);
    }

    public DatosRespuestaConsulta obtenerConsulta(Long id) {
        Consulta consulta = consultaRepository.getReferenceById(id);
        return new DatosRespuestaConsulta(consulta);
    }

    public void cancelar(DatosCancelamientoConsulta datos) {
        if(!consultaRepository.existsByIdAndActivoTrue(datos.idConsulta())) {
            throw new ValidacionDeIntegridad("La consulta solicitada no existe o ya fue cancelada");
        }

        validadorCancelamientos.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());

        consulta.cancelar(datos.motivoCancelamiento());
    }
}
