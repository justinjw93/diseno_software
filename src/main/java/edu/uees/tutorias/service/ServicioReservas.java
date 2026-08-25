package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.persistence.RepositorioHorarios;
import edu.uees.tutorias.persistence.RepositorioReservas;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Orquesta los casos de uso de reservas: crear, confirmar, cancelar y
 * reprogramar.
 *
 * <p>No conoce como se guardan los datos ni como se envian las
 * notificaciones: recibe esas colaboraciones por el constructor a
 * traves de sus interfaces ({@link RepositorioReservas},
 * {@link RepositorioHorarios}, {@link Notificador}). Esto es Dependency
 * Inversion Principle aplicado: la clase de mayor nivel (el servicio) no
 * depende de implementaciones concretas de persistencia o de mensajeria,
 * y puede probarse con implementaciones falsas (in-memory) sin levantar
 * infraestructura real.</p>
 *
 * <p>Tampoco decide directamente si un horario esta disponible ni cambia
 * el estado de una reserva: delega esas reglas en {@link HorarioTutoria}
 * y en {@link Reserva}, que son quienes deben protegerlas (alta
 * cohesion: cada clase resuelve lo que le corresponde).</p>
 */
public class ServicioReservas {

    private final RepositorioReservas repositorioReservas;
    private final RepositorioHorarios repositorioHorarios;
    private final Notificador notificador;

    public ServicioReservas(RepositorioReservas repositorioReservas,
                             RepositorioHorarios repositorioHorarios,
                             Notificador notificador) {
        this.repositorioReservas = Objects.requireNonNull(repositorioReservas);
        this.repositorioHorarios = Objects.requireNonNull(repositorioHorarios);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public Reserva crearReserva(Estudiante estudiante, String idHorario) {
        HorarioTutoria horario = obtenerHorario(idHorario);

        Reserva reserva = new Reserva(
                UUID.randomUUID().toString(), estudiante, horario, LocalDateTime.now());
        repositorioReservas.guardar(reserva);

        notificador.notificar(
                horario.getDocente().getCorreo(),
                "Nueva reserva de tutoria",
                estudiante.nombreCompleto() + " reservo el horario " + horario.getId()
                        + " de " + horario.getAsignatura());

        return reserva;
    }

    public void confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReserva(idReserva);
        reserva.confirmar();
        notificador.notificar(
                reserva.getEstudiante().getCorreo(),
                "Tutoria confirmada",
                "Tu reserva " + reserva.getId() + " fue confirmada.");
    }

    public void cancelarReserva(String idReserva) {
        Reserva reserva = obtenerReserva(idReserva);
        reserva.cancelar();
        notificador.notificar(
                reserva.getHorario().getDocente().getCorreo(),
                "Reserva cancelada",
                "La reserva " + reserva.getId() + " fue cancelada por el estudiante.");
    }

    public void reprogramarReserva(String idReserva, String idNuevoHorario) {
        Reserva reserva = obtenerReserva(idReserva);
        HorarioTutoria nuevoHorario = obtenerHorario(idNuevoHorario);
        reserva.reprogramar(nuevoHorario);
        notificador.notificar(
                reserva.getEstudiante().getCorreo(),
                "Tutoria reprogramada",
                "Tu reserva " + reserva.getId() + " se movio al horario " + nuevoHorario.getId());
    }

    private HorarioTutoria obtenerHorario(String idHorario) {
        return repositorioHorarios.buscarPorId(idHorario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el horario " + idHorario));
    }

    private Reserva obtenerReserva(String idReserva) {
        Optional<Reserva> reserva = repositorioReservas.buscarPorId(idReserva);
        return reserva.orElseThrow(() -> new IllegalArgumentException(
                "No existe la reserva " + idReserva));
    }
}
