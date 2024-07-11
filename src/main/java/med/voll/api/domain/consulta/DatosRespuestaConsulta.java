package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DatosRespuestaConsulta(Long id,
                                     Long idPaciente,
                                     Long idMedico,
                                     LocalDateTime fecha,
                                     Boolean activo,
                                     MotivoCancelamiento motivoCancelamiento) {
    public DatosRespuestaConsulta(Consulta consulta) {
        this(consulta.getId(),consulta.getPaciente().getId(), consulta.getMedico().getId(), consulta.getFecha(),consulta.getActivo(), consulta.getMotivoCancelamiento());
    }
}
