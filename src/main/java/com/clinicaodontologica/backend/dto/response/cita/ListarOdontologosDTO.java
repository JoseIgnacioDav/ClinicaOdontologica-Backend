package com.clinicaodontologica.backend.dto.response.cita;

public class ListarOdontologosDTO {
    Long id;
    String nombres;


    public ListarOdontologosDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
