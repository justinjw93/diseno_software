package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Representa la materia o asignatura asociada a un horario de tutoria.
 * Es una clase de datos simple: no conoce reservas ni horarios.
 */
public class Asignatura {

    private final String codigo;
    private final String nombre;

    public Asignatura(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asignatura that)) return false;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
