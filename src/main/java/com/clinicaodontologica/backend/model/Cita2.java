package com.clinicaodontologica.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
public class Cita2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // datos de la cita
    private LocalDate fecha;
    private LocalTime hora;
    private String estado = "PENDIENTE";
    private LocalTime horadecreaciondelacita;
    private LocalDate fechadecreaciondelacita;

    //  ESTOS CAMPOS SON LOS TEMPORALES PARA VALIDACIÓN: no se guardan ni el email ni la contrasena por que ya estan en  usuario y es mala practica solo los usas para validar los datos no necesitan guardarse en la bdd
    @Transient
    private String emailqueintentacrearlacita;
    @Transient
    private String passworddelmailquetratadecrearlacita;

    // relaciones con la bdd de usuarios
    @ManyToOne// relacion many to one por que un odontologo puede tener muchas citas
    @JoinColumn(name = "odontologo_id", nullable = false) // con el nullable hacemos que una cita siempre tenga que tener un odontolog
    private Usuario odontologo;
    // tambien paciente
    @ManyToOne // relacion many to ine por que un paciente puede tener muchas citas a lo largo del tiempo
    @JoinColumn(name = "paciente_id", nullable = false) // con esto hacemos que una cita siempre tenga que tener un paciente
    private Usuario paciente;


    //constructor vacio para decirle a spring concretamente a hibernate para que pueda reconstruir datos
    public Cita2(){}

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

    public String getEmailqueintentacrearlacita() {
        return emailqueintentacrearlacita;
    }

    public void setEmailqueintentacrearlacita(String emailqueintentacrearlacita) {
        this.emailqueintentacrearlacita = emailqueintentacrearlacita;
    }

    public String getPassworddelmailquetratadecrearlacita() {
        return passworddelmailquetratadecrearlacita;
    }

    public void setPassworddelmailquetratadecrearlacita(String passworddelmailquetratadecrearlacita) {
        this.passworddelmailquetratadecrearlacita = passworddelmailquetratadecrearlacita;
    }

    public void setFechadecreaciondelacita(LocalDate fechadecreaciondelacita) {
        this.fechadecreaciondelacita = fechadecreaciondelacita;


    }
}
