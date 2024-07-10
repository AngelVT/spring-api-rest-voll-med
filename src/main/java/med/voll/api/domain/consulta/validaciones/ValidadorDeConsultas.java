package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosConsulta;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;

public interface ValidadorDeConsultas {
    public void validar(DatosConsulta datos);
}
