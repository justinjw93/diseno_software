package edu.uees.tutorias.domain;

/**
 * Estados posibles de una {@link Reserva} y transiciones validas.
 */
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA,
    REALIZADA
}
