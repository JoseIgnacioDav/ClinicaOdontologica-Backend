package com.clinicaodontologica.backend.controller;

import com.clinicaodontologica.backend.dto.request.ConsultaCitaRequestDTO;
import com.clinicaodontologica.backend.model.Cita2;
import com.clinicaodontologica.backend.service.CitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService){
        this.citaService = citaService;
    }

    @PostMapping("/crearcita")
    public ResponseEntity<?> crearcita (@RequestBody Cita2 citaqueentraporfront){
        try {
            Cita2 citacreada = citaService.crearcita(citaqueentraporfront);
            // si se ejecuta bien chiill
            return ResponseEntity.status(HttpStatus.CREATED).body(citacreada);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }

    }

    @PostMapping("/odontologo/consultarcita") // {TODO}} Confidencial solo permitir con usuario tipo odontologo por token loco
    /*public ResponseEntity<?> consultarcita(@RequestBody Usuario odontologo, @Requestparam LocalDate fecha){ // aqui te tocaba meter
    request param por que solo puede tener un bodu asi que usaremos un dto para mandar todod dentro del body
        return ResponseEntity.ok(citaService.disponibilidadOdontologo(odontologo,fecha));
    }*/
                                            // {todo} tengo que poner una logica para que el odontologo solo pueda consultar sus propias citas despues
   public ResponseEntity<?> consultarcita(@RequestBody ConsultaCitaRequestDTO request){ // aqui le pides directamente al dto spring lo hace automatico abre el dto literal solo con el constructor y los getters y setters lo hace solito
       //aqui extraogo o que necesito del dto
        return ResponseEntity.ok(citaService.disponibilidadOdontologo(request.getOdontologo(),request.getFecha()));

    }


    @PostMapping("/paciente/consultarcita")
    public String saludo(){
       return "hola";
    }




}
