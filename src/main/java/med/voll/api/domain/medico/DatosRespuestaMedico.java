package med.voll.api.domain.medico;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRespuestaMedico(
        Long id,
        String nombre,
        String email,
        String documento,
        String telefono,
        DatosDireccion direccion) {

    public DatosRespuestaMedico(Medico medico) {
        this(medico.getId(),
        medico.getNombre(),
        medico.getEmail(),
        medico.getDocumento(),
        medico.getTelefono(),
        new DatosDireccion(medico.getDireccion()));
    }
}
