# Análisis del dominio, diseño OO, cohesión/acoplamiento y SOLID

Sistema de gestión de tutorías — Actividad 5 | Ae1 (UCOM0310, Semana 2)

## Parte 1 · Análisis del dominio

| Elemento / clase candidata | Responsabilidad | Información relevante | Reglas / colaboraciones |
|---|---|---|---|
| Estudiante | Solicitar, consultar y cancelar tutorías | id, nombre, apellido, correo, carrera, semestre | Colabora con Reserva; hereda de Usuario |
| Docente | Publicar y administrar sus horarios y asignaturas | id, nombre, apellido, correo, especialidad, lista de asignaturas | Colabora con Asignatura y HorarioTutoria; hereda de Usuario |
| Asignatura | Representar la materia asociada a una tutoría | código, nombre | Colabora con Docente y HorarioTutoria |
| HorarioTutoria | Representar un bloque de tutoría y proteger su disponibilidad | docente, asignatura, fecha, hora inicio/fin, estado (DISPONIBLE, RESERVADO, INACTIVO) | Regla: un horario reservado no puede volver a reservarse; solo el propio horario cambia su estado |
| Reserva | Gestionar el ciclo de vida completo de una solicitud de tutoría | estudiante, horario, fecha de creación, estado (PENDIENTE, CONFIRMADA, CANCELADA, REALIZADA) | Colabora con Estudiante y HorarioTutoria; regla: solo transiciones válidas de estado, y mantiene consistente al horario asociado |
| ServicioReservas | Coordinar los casos de uso de reservar, confirmar, cancelar y reprogramar | ninguna propia; opera sobre Reserva y HorarioTutoria | Colabora con RepositorioReservas, RepositorioHorarios y Notificador (recibidos por constructor) |
| RepositorioReservas / RepositorioHorarios | Abstraer la persistencia de reservas y horarios | — | Colaboran con ServicioReservas; implementados en esta iteración en memoria |
| Notificador | Abstraer el envío de notificaciones a los usuarios | destinatario, asunto, mensaje | Colabora con ServicioReservas; implementado por NotificadorConsola y NotificadorLog |

**Elementos adicionales identificados:** además de los cinco elementos mínimos (Estudiante, Docente, HorarioTutoria, Reserva, Notificador), se agregaron `Asignatura` (para no forzar a HorarioTutoria a guardar el nombre de la materia como texto suelto), `ServicioReservas` (para no dejar la orquestación del caso de uso dentro de Reserva u otra clase de dominio) y `RepositorioReservas` / `RepositorioHorarios` (para separar la persistencia, que es infraestructura, de la lógica de negocio).

**Dominio vs. infraestructura:** confirmar, cancelar, reprogramar y verificar disponibilidad son lógica de dominio (viven en `Reserva` y `HorarioTutoria`, no dependen de ninguna tecnología). Guardar en una base de datos y enviar un correo o una notificación push son infraestructura: se representan mediante `RepositorioReservas`/`RepositorioHorarios` y `Notificador`, exactamente para que un cambio de tecnología no obligue a tocar la lógica de negocio.

## Parte 2 · Diseño orientado a objetos

| Clase | Responsabilidad | Atributos principales | Comportamientos | Colabora con |
|---|---|---|---|---|
| Usuario (abstracta) | Representar los datos comunes de cualquier persona del sistema | id, nombre, apellido, correo | nombreCompleto() | — |
| Estudiante | Especializar a Usuario para quien solicita tutorías | carrera, semestre | (hereda de Usuario) | Reserva |
| Docente | Especializar a Usuario para quien publica horarios | especialidad, asignaturas | agregarAsignatura() | Asignatura, HorarioTutoria |
| HorarioTutoria | Proteger su propio estado de disponibilidad | docente, asignatura, fecha, horaInicio, horaFin, estado | estaDisponible(), marcarReservado(), liberar(), marcarInactivo() | Docente, Asignatura, Reserva |
| Reserva | Gestionar el ciclo de vida de una solicitud de tutoría | estudiante, horario, fechaCreacion, estado | confirmar(), cancelar(), marcarRealizada(), reprogramar() | Estudiante, HorarioTutoria |
| RepositorioReservas / RepositorioHorarios (interfaces) | Definir el contrato de persistencia | — | guardar(), buscarPorId(), listarPorEstudiante() / listarDisponiblesPorDocente() | Reserva / HorarioTutoria |
| Notificador (interfaz) | Definir el contrato de envío de notificaciones | — | notificar(destinatario, asunto, mensaje) | — |
| ServicioReservas | Orquestar crear/confirmar/cancelar/reprogramar reservas | repositorioReservas, repositorioHorarios, notificador | crearReserva(), confirmarReserva(), cancelarReserva(), reprogramarReserva() | RepositorioReservas, RepositorioHorarios, Notificador, Reserva |

**Encapsulación:** ninguna clase expone un `setEstado(...)` público. `Reserva` y `HorarioTutoria` son las únicas responsables de cambiar su propio estado, a través de métodos que validan si la transición es válida (por ejemplo, `Reserva.cancelar()` solo funciona desde PENDIENTE o CONFIRMADA, y libera automáticamente el horario asociado).

**Herencia vs. composición:** `Estudiante` y `Docente` heredan de `Usuario` porque ambos *son* un usuario del sistema y ninguna subclase necesita romper el contrato de la clase base (cumplen LSP: en ningún caso una subclase lanza una excepción por un método heredado). En cambio, `ServicioReservas` no hereda de `RepositorioReservas` ni de `Notificador`: los usa por **composición**, porque la relación real es "necesita colaborar con", no "es un".

## Parte 3 · Cohesión y acoplamiento aplicados al diseño propuesto

**Decisiones que favorecen la cohesión.** Cada clase del proyecto resuelve un único motivo de cambio: `HorarioTutoria` solo conoce reglas de disponibilidad (`estaDisponible()`, `marcarReservado()`, `liberar()`); `Reserva` solo conoce el ciclo de vida de una solicitud (`confirmar()`, `cancelar()`, `marcarRealizada()`, `reprogramar()`); `ServicioReservas` solo orquesta el caso de uso, sin validar directamente disponibilidad ni guardar datos. Ninguna clase mezcla, como en el caso `SistemaTutorias` analizado en semanas anteriores, reservas con reportes, correos y acceso a base de datos a la vez.

**Dependencias que podrían producir alto acoplamiento.** Si `ServicioReservas` hubiera declarado campos de tipo `RepositorioReservasEnMemoria`, `RepositorioHorariosEnMemoria` o `NotificadorConsola` en lugar de sus interfaces, cualquier cambio de tecnología (por ejemplo, pasar a una base de datos relacional) habría obligado a modificar el servicio y habría hecho imposible probarlo sin esa tecnología concreta.

**Dependencias concretas reemplazadas por abstracciones.** Esto es exactamente lo que se evitó: `ServicioReservas` (véase `service/ServicioReservas.java`) recibe `RepositorioReservas`, `RepositorioHorarios` y `Notificador` por constructor, y solo `App.java` (el *composition root*) decide qué implementación concreta usar (`RepositorioReservasEnMemoria`, `RepositorioHorariosEnMemoria`, `NotificadorConsola`). Las pruebas unitarias (`ServicioReservasTest`) aprovechan esto mismo: usan un `Notificador` de prueba (`NotificadorDePrueba`) sin tocar ninguna tecnología real.

**Efecto de un cambio de tecnología.** Si la UEES decidiera migrar la persistencia a una base de datos relacional, bastaría con crear `RepositorioReservasJdbc implements RepositorioReservas` e inyectarla en `App.java`; `ServicioReservas`, `Reserva` y `HorarioTutoria` no cambiarían ni una línea. Lo mismo ocurre si se reemplaza la notificación por un proveedor SMTP real: basta una nueva clase que implemente `Notificador` (tal como ya se hizo con `NotificadorLog`, agregado sin modificar `ServicioReservas`, ver el commit `refactor: incorporar notificador adicional sin modificar el servicio`).

## Parte 4 · Principios SOLID aplicados

**1. Dependency Inversion Principle (DIP).** `ServicioReservas` no depende de `RepositorioReservasEnMemoria`, `RepositorioHorariosEnMemoria` ni de `NotificadorConsola`; su constructor solo conoce `RepositorioReservas`, `RepositorioHorarios` y `Notificador` (interfaces). Esto evita que un cambio de base de datos o de proveedor de correo se propague a la lógica de negocio, y es lo que permite probar el servicio con dobles de prueba (`ServicioReservasTest`) sin infraestructura real.

**2. Single Responsibility Principle (SRP).** `HorarioTutoria` y `Reserva` concentran cada una una sola responsabilidad y, por lo tanto, una sola razón para cambiar: `HorarioTutoria` cambia solo si cambian las reglas de disponibilidad de un bloque de tutoría; `Reserva` cambia solo si cambian las reglas del ciclo de vida de una solicitud. Esto evita el problema evidenciado en el análisis de la clase `SistemaTutorias` (semanas 1 y 2), donde una sola clase concentraba usuarios, reservas, correo, reportes y base de datos.

**3. Open/Closed Principle (OCP).** Se agregó `NotificadorLog` como una segunda implementación de `Notificador` sin modificar `ServicioReservas` ni `Notificador` (commit `refactor: incorporar notificador adicional sin modificar el servicio`). El servicio queda abierto a nuevas formas de notificación (SMS, push, un canal de auditoría) y cerrado a modificación en su código existente.

**4. Interface Segregation Principle (ISP).** En lugar de una única interfaz `Repositorio` con métodos de reservas y de horarios mezclados, se definieron `RepositorioReservas` y `RepositorioHorarios` por separado: una clase que solo necesita consultar horarios (por ejemplo, para mostrarle disponibilidad a un estudiante) nunca depende de operaciones de reservas que no le corresponden.

## Conclusiones

El sistema pasó del análisis narrativo del caso de tutorías a un modelo orientado a objetos en el que cada clase tiene una sola responsabilidad (Usuario/Estudiante/Docente, HorarioTutoria, Reserva) y donde las dependencias hacia infraestructura (persistencia y notificaciones) quedaron detrás de interfaces (RepositorioReservas, RepositorioHorarios, Notificador), siguiendo el principio de inversión de dependencias. La alta cohesión y el bajo acoplamiento no son una declaración teórica: se comprueban en la práctica: ServicioReservas puede probarse por completo con implementaciones en memoria y un notificador de prueba, y agregar NotificadorLog no exigió modificar ninguna clase existente. El principal aprendizaje de la actividad fue que la cohesión y el acoplamiento dejan de ser conceptos abstractos en cuanto se aplican a decisiones concretas: qué clase protege cada estado, qué depende de qué interfaz, y qué se puede sustituir sin romper el resto del sistema.
