package med.voll.api.domain.consulta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Page<Consulta> findByActivoTrue(Pageable paginacion);

    Boolean existsByPacienteIdAndFechaBetweenAndActivoTrue(Long id, LocalDateTime primerHorario, LocalDateTime ultimoHorario);

    Boolean existsByMedicoIdAndFechaAndActivoTrue(Long id, LocalDateTime fecha);

    Boolean existsByIdAndActivoTrue(Long id);

    @Query(value = """
                SELECT COUNT(*) > 0 FROM Consultas c
                WHERE :fechaSolicitada BETWEEN c.fecha AND DATE_ADD(c.fecha, INTERVAL 1 HOUR)
                AND c.medico_id = :medicoId AND c.activo = 1
    """, nativeQuery = true)
    Integer existsFechaWithinOneHour(Long medicoId,LocalDateTime fechaSolicitada);
}
