package med.voll.api.domain.paciente;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRespuestaPaciente(
        Long id,
        String nombre,
        String email,
        String documentoIdentidad,
        String telefono,
        DatosDireccion direccion) {

    public DatosRespuestaPaciente(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getDocumentoIdentidad(),
                paciente.getTelefono(),
                new DatosDireccion(paciente.getDireccion()));
    }
}
