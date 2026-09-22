package com.clinicaodontologica.backend.exception;

import java.time.LocalDateTime;

// creo el dto para el manejo de errores globales, para ya no crear nuevos siempre
public class ErrorRespuestaDTO {
    private LocalDateTime timestamp;
    private int estado;
    private String error;
    private String mensaje;

    public ErrorRespuestaDTO(LocalDateTime timestamp, int estado, String error, String mensaje){
        this.timestamp = timestamp;
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
    }

    //getters


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }
}
