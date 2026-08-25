package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Gestiona el ciclo de vida de una solicitud de tutoria entre un
 * {@link Estudiante} y un {@link HorarioTutoria}.
 *
 * <p>Ninguna clase externa cambia el estado directamente (no existe un
 * {@code setEstado(...)} publico): cada transicion pasa por un metodo
 * que valida si es valida desde el estado actual y que mantiene
 * consistente al {@link HorarioTutoria} asociado. Esto responde a la
 * pregunta de analisis "¿quien deberia cambiar el estado de una
 * reserva?": la propia Reserva, nunca un componente externo.</p>
 */
public class Reserva {

    private final String id;
    private final Estudiante estudiante;
    private HorarioTutoria horario;
    private final LocalDateTime fechaCreacion;
    private EstadoReserva estado;

    public Reserva(String id, Estudiante estudiante, HorarioTutoria horario,
                    LocalDateTime fechaCreacion) {
        this.id = Objects.requireNonNull(id);
        this.estudiante = Objects.requireNonNull(estudiante);
        this.horario = Objects.requireNonNull(horario);
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion);
        this.horario.marcarReservado();
        this.estado = EstadoReserva.PENDIENTE;
    }

    public void confirmar() {
        exigirEstado(EstadoReserva.PENDIENTE, "confirmar");
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado != EstadoReserva.PENDIENTE && estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "La reserva " + id + " no puede cancelarse desde el estado " + estado);
        }
        this.estado = EstadoReserva.CANCELADA;
        this.horario.liberar();
    }

    public void marcarRealizada() {
        exigirEstado(EstadoReserva.CONFIRMADA, "marcar como realizada");
        this.estado = EstadoReserva.REALIZADA;
    }

    /**
     * Reprograma la reserva a un nuevo horario: libera el horario
     * actual, ocupa el nuevo y regresa la reserva a PENDIENTE (requiere
     * una nueva confirmacion).
     */
    public void reprogramar(HorarioTutoria nuevoHorario) {
        if (estado != EstadoReserva.PENDIENTE && estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "La reserva " + id + " no puede reprogramarse desde el estado " + estado);
        }
        Objects.requireNonNull(nuevoHorario);
        nuevoHorario.marcarReservado();
        this.horario.liberar();
        this.horario = nuevoHorario;
        this.estado = EstadoReserva.PENDIENTE;
    }

    private void exigirEstado(EstadoReserva esperado, String accion) {
        if (estado != esperado) {
            throw new IllegalStateException(
                    "No se puede " + accion + " la reserva " + id + " desde el estado " + estado);
        }
    }

    public String getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public HorarioTutoria getHorario() {
        return horario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}
