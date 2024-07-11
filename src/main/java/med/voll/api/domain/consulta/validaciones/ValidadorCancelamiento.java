package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.DatosConsulta;

public interface ValidadorCancelamiento {
    public void validar(DatosCancelamientoConsulta datos);
}
