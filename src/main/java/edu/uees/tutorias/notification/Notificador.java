package edu.uees.tutorias.notification;

/**
 * Contrato para el envio de notificaciones a los usuarios del sistema.
 *
 * <p>{@code ServicioReservas} depende de esta abstraccion, no de un
 * mecanismo de envio concreto (correo SMTP, SMS, push). Cualquier nuevo
 * canal de notificacion se agrega implementando esta interfaz, sin
 * modificar el servicio que la utiliza (Open/Closed + Dependency
 * Inversion).</p>
 */
public interface Notificador {

    void notificar(String destinatario, String asunto, String mensaje);
}
