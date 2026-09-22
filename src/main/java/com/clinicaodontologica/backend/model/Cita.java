package com.clinicaodontologica.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas", uniqueConstraints = { // con uniqueconstraints me evito las race codnitions y que se dupliquen citas en la msima fecha y hora
        @UniqueConstraint(columnNames = {"odontologo_id","fecha","hora"})
})
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // datos de la cita
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;
    @Column(nullable = false)
    private String estado = "PENDIENTE";   // pendiente -> todo // confirmada // completada // cancelada
    @Column(nullable = false,updatable = false)
    private LocalTime horadecreaciondelacita;
    @Column(nullable = false, updatable = false)
    private LocalDate fechadecreaciondelacita;
    @PrePersist
    protected void onCreate() {
        this.fechadecreaciondelacita = LocalDate.now();
        this.horadecreaciondelacita = LocalTime.now();
    }

    //  ESTOS CAMPOS SON LOS TEMPORALES PARA VALIDACIÓN: no se guardan ni el email ni la contrasena por que ya estan en  usuario y es mala practica solo los usas para validar los datos no necesitan guardarse en la bdd
    // no se les puede poner nullavle ni nada de restricciones de bdd

    // eliminacion de los atributos para validar la creacion de las citas, ahora se hace con cookies y por rol

    // relaciones con la bdd de usuarios
    @ManyToOne// relacion many to one por que un odontologo puede tener muchas citas
    @JoinColumn(name = "odontologo_id", nullable = false) // con el nullable hacemos que una cita siempre tenga que tener un odontolog
    private Usuario odontologo;
    // tambien paciente
    @ManyToOne // relacion many to ine por que un paciente puede tener muchas citas a lo largo del tiempo
    @JoinColumn(name = "paciente_id", nullable = false) // con esto hacemos que una cita siempre tenga que tener un paciente
    private Usuario paciente;


    //constructor vacio para decirle a spring concretamente a hibernate para que pueda reconstruir datos
    public Cita(){}

    //getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(Usuario odontologo) {
        this.odontologo = odontologo;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public LocalTime getHoradecreaciondelacita() {
        return horadecreaciondelacita;
    }

    public void setHoradecreaciondelacita(LocalTime horadecreaciondelacita) {
        this.horadecreaciondelacita = horadecreaciondelacita;
    }

    public LocalDate getFechadecreaciondelacita() {
        return fechadecreaciondelacita;
    }


    public void setFechadecreaciondelacita(LocalDate fechadecreaciondelacita) {
        this.fechadecreaciondelacita = fechadecreaciondelacita;


    }
}
