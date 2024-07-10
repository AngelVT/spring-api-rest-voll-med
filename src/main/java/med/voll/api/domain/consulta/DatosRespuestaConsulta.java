package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Especialidad;

import java.time.LocalDateTime;

public record DatosRespuestaConsulta(Long id,
                                     Long idPaciente,
                                     Long idMedico,
                                     LocalDateTime fecha) {
}
