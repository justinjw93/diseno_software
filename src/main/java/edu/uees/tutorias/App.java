package edu.uees.tutorias;

import edu.uees.tutorias.domain.Asignatura;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;
import edu.uees.tutorias.persistence.RepositorioHorarios;
import edu.uees.tutorias.persistence.RepositorioHorariosEnMemoria;
import edu.uees.tutorias.persistence.RepositorioReservas;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import edu.uees.tutorias.service.ServicioReservas;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Punto de entrada de demostracion: arma las dependencias del sistema
 * (composition root) y ejecuta un flujo tipico de reserva, confirmacion
 * y cancelacion de una tutoria.
 */
public final class App {

    public static void main(String[] args) {
        RepositorioReservas repositorioReservas = new RepositorioReservasEnMemoria();
        RepositorioHorarios repositorioHorarios = new RepositorioHorariosEnMemoria();
        Notificador notificador = new NotificadorConsola();
        ServicioReservas servicioReservas =
                new ServicioReservas(repositorioReservas, repositorioHorarios, notificador);

        Docente docente = new Docente("D001", "Ana", "Perez", "ana.perez@uees.edu.ec", "Bases de datos");
        Asignatura asignatura = new Asignatura("SIS201", "Bases de datos");
        docente.agregarAsignatura(asignatura);

        HorarioTutoria horario = new HorarioTutoria(
                "H001", docente, asignatura,
                LocalDate.of(2026, 9, 1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        repositorioHorarios.guardar(horario);

        Estudiante estudiante = new Estudiante(
                "E001", "Justin", "Arreaga", "justin.arreaga@uees.edu.ec", "Computacion", 5);

        Reserva reserva = servicioReservas.crearReserva(estudiante, horario.getId());
        repositorioReservas.guardar(reserva);

        servicioReservas.confirmarReserva(reserva.getId());
        servicioReservas.cancelarReserva(reserva.getId());

        System.out.println("Estado final de la reserva: " + reserva.getEstado());
        System.out.println("Estado final del horario: " + horario.getEstado());
    }

    private App() {
    }
}
