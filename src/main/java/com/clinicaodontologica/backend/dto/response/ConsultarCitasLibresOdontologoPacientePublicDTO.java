package com.clinicaodontologica.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultarCitasLibresOdontologoPacientePublicDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    //aqui mandamos texto plano en lugar del objeto con demasiada info
    private String nombreOdontologo;

    //constructor vacio obligatorio
    public ConsultarCitasLibresOdontologoPacientePublicDTO(){}

    //getters y setters:


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
}
