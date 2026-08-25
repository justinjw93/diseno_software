package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.HorarioTutoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Implementacion de {@link RepositorioHorarios} en memoria. */
public class RepositorioHorariosEnMemoria implements RepositorioHorarios {

    private final Map<String, HorarioTutoria> horarios = new LinkedHashMap<>();

    @Override
    public void guardar(HorarioTutoria horario) {
        horarios.put(horario.getId(), horario);
    }

    @Override
    public Optional<HorarioTutoria> buscarPorId(String id) {
        return Optional.ofNullable(horarios.get(id));
    }

    @Override
    public List<HorarioTutoria> listarDisponiblesPorDocente(Docente docente) {
        return horarios.values().stream()
                .filter(h -> h.getDocente().equals(docente) && h.estaDisponible())
                .collect(Collectors.toList());
    }
}
