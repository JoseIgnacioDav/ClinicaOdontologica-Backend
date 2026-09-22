package com.clinicaodontologica.backend.dto.response.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultarcitasagendadaspacienteDTO {
    Long id;
    LocalDate fecha;
    LocalTime hora;
    String odontologo;
    String estado;

    public ConsultarcitasagendadaspacienteDTO(){}

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

    public String getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(String odontologo) {
        this.odontologo = odontologo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
