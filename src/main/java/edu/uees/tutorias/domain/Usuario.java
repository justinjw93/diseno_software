package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Representa los datos comunes de cualquier persona que participa en el
 * sistema (estudiante o docente).
 *
 * <p>Es una clase abstracta: nunca se instancia directamente, solo a
 * traves de sus especializaciones. Concentra unicamente la
 * responsabilidad de identificar a una persona dentro del sistema; no
 * conoce reservas, horarios ni mecanismos de notificacion.</p>
 */
public abstract class Usuario {

    private final String id;
    private final String nombre;
    private final String apellido;
    private final String correo;

    protected Usuario(String id, String nombre, String apellido, String correo) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser nulo");
        this.apellido = Objects.requireNonNull(apellido, "apellido no puede ser nulo");
        this.correo = Objects.requireNonNull(correo, "correo no puede ser nulo");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
