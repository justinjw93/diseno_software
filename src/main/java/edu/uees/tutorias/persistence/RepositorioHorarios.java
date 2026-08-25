package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.HorarioTutoria;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia de horarios de tutoria, separado de
 * {@link RepositorioReservas} porque representa un objeto de dominio
 * distinto con su propio ciclo de vida (interface segregation: quien
 * solo necesita horarios no depende de operaciones de reservas).
 */
public interface RepositorioHorarios {

    void guardar(HorarioTutoria horario);

    Optional<HorarioTutoria> buscarPorId(String id);

    List<HorarioTutoria> listarDisponiblesPorDocente(Docente docente);
}
