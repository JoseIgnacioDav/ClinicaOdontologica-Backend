package com.clinicaodontologica.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultarCitasOdontologoConfidentialResponseDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    //aqui mandamos texto plano en lugar del objeto completo con contrasenas
    private String nombreOdontologo;
    private String nombrePaciente;
    private String cedulapaciente;
    private String emailpaciente;

    //Constructor vacio obligatorio
    public ConsultarCitasOdontologoConfidentialResponseDTO(){}

    // getters y setters

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

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public void setNombreOdontologo(String nombreOdontologo) {
        this.nombreOdontologo = nombreOdontologo;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getCedulapaciente() {
        return cedulapaciente;
    }

    public void setCedulapaciente(String cedulapaciente) {
        this.cedulapaciente = cedulapaciente;
    }

    public String getEmailpaciente() {
        return emailpaciente;
    }

    public void setEmailpaciente(String emailpaciente) {
        this.emailpaciente = emailpaciente;
    }
}
