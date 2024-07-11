package med.voll.api.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaRepository consultaRepository;
    private final ConsultaAgendaService consultaAgendaService;

    @Autowired
    public ConsultaController(ConsultaRepository consultaRepository, ConsultaAgendaService consultaAgendaService) {
        this.consultaRepository = consultaRepository;
        this.consultaAgendaService = consultaAgendaService;
    }

    @PostMapping
    public ResponseEntity agendar(@RequestBody @Valid DatosConsulta datosConsulta) {
        var response = consultaAgendaService.agendar(datosConsulta);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoConsulta>> obtenerConsultas(@PageableDefault(size = 10, sort = "fecha") Pageable paginacion) {
        var response = consultaAgendaService.obtenerConsultas(paginacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaConsulta> obtenerConsultas(@PathVariable Long id) {
        var response = consultaAgendaService.obtenerConsulta(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DatosCancelamientoConsulta datos) {
        consultaAgendaService.cancelar(datos);
        return ResponseEntity.noContent().build();
    }
}
