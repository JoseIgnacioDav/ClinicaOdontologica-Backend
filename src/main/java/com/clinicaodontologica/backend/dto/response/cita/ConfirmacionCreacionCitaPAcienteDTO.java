package com.clinicaodontologica.backend.dto.response.cita;
// aqui voy a hacer que deje de devolver toda la informacion al momento de crear el usuario
// quiero mantener la info para el dashboard de usuario asi muestra en pantalla info que ya tiene plan el nombre del man la cedula y todo en un panel wonito

import java.time.LocalDate;
import java.time.LocalTime;

public class ConfirmacionCreacionCitaPAcienteDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String nombreodontologo;
    private String nombrepaciente;

    public ConfirmacionCreacionCitaPAcienteDTO(){}

    // creacion de constructores

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

    public String getNombreodontologo() {
        return nombreodontologo;
    }

    public void setNombreodontologo(String nombreodontologo) {
        this.nombreodontologo = nombreodontologo;
    }

    public String getNombrepaciente() {
        return nombrepaciente;
    }

    public void setNombrepaciente(String nombrepaciente) {
        this.nombrepaciente = nombrepaciente;
    }
}
