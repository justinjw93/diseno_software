package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementacion de {@link RepositorioReservas} en memoria, util para
 * pruebas y para esta primera iteracion del proyecto sin depender de un
 * motor de base de datos. Puede sustituirse por una implementacion JDBC
 * o JPA sin modificar {@code ServicioReservas}.
 */
public class RepositorioReservasEnMemoria implements RepositorioReservas {

    private final Map<String, Reserva> reservas = new LinkedHashMap<>();

    @Override
    public void guardar(Reserva reserva) {
        reservas.put(reserva.getId(), reserva);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) {
        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public List<Reserva> listarPorEstudiante(Estudiante estudiante) {
        return reservas.values().stream()
                .filter(r -> r.getEstudiante().equals(estudiante))
                .collect(Collectors.toList());
    }
}
