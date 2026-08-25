package edu.uees.tutorias.domain;

/**
 * Estudiante que solicita, consulta y cancela tutorias.
 *
 * <p>Hereda de {@link Usuario} porque un estudiante ES-UN usuario del
 * sistema: comparte identidad, nombre y correo, y extiende ese contrato
 * sin romper las expectativas de la clase base (principio de sustitucion
 * de Liskov). No conoce como se persisten sus reservas ni como se le
 * notifica; esas responsabilidades pertenecen a otras clases.</p>
 */
public class Estudiante extends Usuario {

    private final String carrera;
    private final int semestre;

    public Estudiante(String id, String nombre, String apellido, String correo,
                       String carrera, int semestre) {
        super(id, nombre, apellido, correo);
        this.carrera = carrera;
        this.semestre = semestre;
    }

    public String getCarrera() {
        return carrera;
    }

    public int getSemestre() {
        return semestre;
    }
}
