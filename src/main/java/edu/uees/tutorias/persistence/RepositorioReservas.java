package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia de reservas.
 *
 * <p>{@code ServicioReservas} depende unicamente de esta interfaz, no de
 * una tecnologia concreta (Dependency Inversion Principle). Cambiar de
 * almacenamiento en memoria a una base de datos relacional, por ejemplo,
 * implica escribir una nueva implementacion de este contrato sin tocar
 * la logica de negocio.</p>
 */
public interface RepositorioReservas {

    void guardar(Reserva reserva);

    Optional<Reserva> buscarPorId(String id);

    List<Reserva> listarPorEstudiante(Estudiante estudiante);
}
