package med.voll.api.controllers;

import jakarta.validation.Valid;
import med.voll.api.domain.consulta.ConsultaAgendaService;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosConsulta;
import med.voll.api.domain.consulta.DatosRespuestaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        consultaAgendaService.agendar(datosConsulta);
        return ResponseEntity.ok(new DatosRespuestaConsulta(null, null,null,null));
    }
}
