package med.voll.api.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoRepository medicoRepository;

    @Autowired
    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @PostMapping
    public String registrarMedico(@RequestBody @Valid DatosMedico datosMedico) {
        medicoRepository.save(new Medico(datosMedico));
        return "Medico ..";
    }

    @GetMapping
    public Page<DatosListadoMedico> listadoMedico(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
//        return medicoRepository.findAll(paginacion)
//                .map(DatosListadoMedico::new);
        return medicoRepository.findByActivoTrue(paginacion)
                .map(DatosListadoMedico::new);
    }

    @PutMapping
    @Transactional
    public String actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return "Actualizado ..";
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivar();
        //medicoRepository.delete(medico);
        return "borrado .. ";
    }
}
