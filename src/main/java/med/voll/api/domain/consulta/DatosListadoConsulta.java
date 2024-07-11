package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DatosListadoConsulta(Long id,
                                   Long idPaciente,
                                   Long idMedico,
                                   LocalDateTime fecha) {
    public DatosListadoConsulta(Consulta consulta) {
        this(consulta.getId(),consulta.getPaciente().getId(), consulta.getMedico().getId(), consulta.getFecha());
    }
}
