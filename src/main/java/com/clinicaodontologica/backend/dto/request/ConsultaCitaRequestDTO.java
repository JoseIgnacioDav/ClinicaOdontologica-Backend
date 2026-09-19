package com.clinicaodontologica.backend.dto.request;

import com.clinicaodontologica.backend.model.Usuario;

import java.time.LocalDate;

public class ConsultaCitaRequestDTO {
    private Usuario odontologo;
    private LocalDate fecha;

    // Constructor vacío (obligatorio si no pones otros, o Java lo crea por defecto si no hay de
    // otros, pero es buena práctica declararlo explícitamente).
    public ConsultaCitaRequestDTO() {}

    public Usuario getOdontologo(){
        return odontologo;
    }
    public void setOdontologo(Usuario odontologo) {
        this.odontologo = odontologo;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


}
