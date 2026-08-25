package edu.uees.tutorias.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Docente que publica y administra horarios de tutoria para una o mas
 * asignaturas.
 *
 * <p>Al igual que {@link Estudiante}, hereda de {@link Usuario} porque
 * comparte su identidad basica; la lista de asignaturas es informacion
 * propia del docente, no del sistema en general.</p>
 */
public class Docente extends Usuario {

    private final String especialidad;
    private final List<Asignatura> asignaturas = new ArrayList<>();

    public Docente(String id, String nombre, String apellido, String correo, String especialidad) {
        super(id, nombre, apellido, correo);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void agregarAsignatura(Asignatura asignatura) {
        if (!asignaturas.contains(asignatura)) {
            asignaturas.add(asignatura);
        }
    }

    public List<Asignatura> getAsignaturas() {
        return Collections.unmodifiableList(asignaturas);
    }
}
