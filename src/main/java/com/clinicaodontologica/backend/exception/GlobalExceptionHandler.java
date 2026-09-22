package com.clinicaodontologica.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //atrapa cuandso se lanza una excepcion de recurso no encontradi (404)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorRespuestaDTO> handleRuntimeException (RuntimeException ex){
        ErrorRespuestaDTO error = new ErrorRespuestaDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // atrapa cualquier error no esperado del sistema 500 -nullpointExcep, fallo en bdd etc..
    @ExceptionHandler(Exception.class)
    public  ResponseEntity<ErrorRespuestaDTO> handleGeneralException(Exception ex){
        ErrorRespuestaDTO error = new ErrorRespuestaDTO(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server error","Ocurrio un error inesperado en el servidor " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
