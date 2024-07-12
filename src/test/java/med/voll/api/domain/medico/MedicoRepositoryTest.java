package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.direccion.Direccion;
import med.voll.api.domain.paciente.DatosPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    private final MedicoRepository medicoRepository;

    private final TestEntityManager em;

    @Autowired
    MedicoRepositoryTest(MedicoRepository medicoRepository, TestEntityManager em) {
        this.medicoRepository = medicoRepository;
        this.em = em;
    }

    @Test
    @DisplayName("Deberia retornar nulo cuando el medico se encuentra en consulta con otro paciente en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {
        var proximoLunes10h = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10).withMinute(0).withSecond(0);

        var medico = registrarMedico("Anacleto", "anacleto@voll.med","1441308002", "123456", Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Miguel", "miguel@gmail.com", "1234567890", "987.654.321-01");
        registrarConsulta(medico, paciente, proximoLunes10h);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA, proximoLunes10h);

        assertThat(medicoLibre).isEmpty();
    }

    @Test
    @DisplayName("Deberia retornar un medico disponible en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {
        var proximoLunes10h = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10).withMinute(0).withSecond(0);

        var medico = registrarMedico("Anacleto", "anacleto@voll.med","1441308002", "123456", Especialidad.CARDIOLOGIA);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA, proximoLunes10h);

        assertThat(medicoLibre).contains(medico);
    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        em.persist(new Consulta(null, medico, paciente,true, null, fecha));
    }

    private Medico registrarMedico(String nombre, String email, String telefono, String documento, Especialidad especialidad) {
        var medico = new Medico(new DatosMedico(nombre,email,telefono,documento,especialidad,new DatosDireccion("calle 1","distrito1","bogota","12","a")));
        em.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String telefono, String documento) {
        var paciente = new Paciente(new DatosPaciente(nombre,email,telefono,documento,new DatosDireccion("calle 1","distrito1","bogota","12","a")));
        em.persist(paciente);
        return paciente;
    }
}