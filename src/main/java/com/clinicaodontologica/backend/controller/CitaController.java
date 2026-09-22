package com.clinicaodontologica.backend.controller;

import com.clinicaodontologica.backend.dto.request.cita.ConsultaCitaRequestDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConfirmacionCreacionCitaPAcienteDTO;
import com.clinicaodontologica.backend.dto.response.usuario.RespuestaAlLoguaarseDTO;
import com.clinicaodontologica.backend.model.Cita;
import com.clinicaodontologica.backend.model.Usuario;
import com.clinicaodontologica.backend.service.CitaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService){
        this.citaService = citaService;
    }
   //borrado de funcion comentada
    @PostMapping("/crearcita")
    public ResponseEntity<?> crearcita (@RequestBody Cita citaquentraporfront, HttpSession session){
        try{
            // verificamos la sesion   // le hacemos un casting para que java sepa que queremos que se porte como un respuestaaloguearseDTO y nos deje jalar el atributo session
            RespuestaAlLoguaarseDTO usuarioSession = (RespuestaAlLoguaarseDTO) session.getAttribute("usuarioLogueado");
            if(usuarioSession == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","no hay una sesion activa porfavor inicia sesion"));
            }
            // llamo al servicio:
            ConfirmacionCreacionCitaPAcienteDTO citacreada = citaService.crearcita(citaquentraporfront, usuarioSession.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(citacreada);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/odontologo/consultarcita") //

   public ResponseEntity<?> consultarcita(@RequestBody ConsultaCitaRequestDTO request,HttpSession session){
       // uso el dto ConsultacitaRequest para poder recibir el odontologo y la fecha en el body
        // solo admin puede ver las citas de otros odontologos, por default los fuerza a ver solo sus paciente
        RespuestaAlLoguaarseDTO usuarioSession = (RespuestaAlLoguaarseDTO) session.getAttribute("usuarioLogueado");
        if(usuarioSession == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","No hay sesion activa"));
        }
        // si hay sesion activa valido el rol
        if(!usuarioSession.getRol().equalsIgnoreCase("ODONTOLOGO")&& !usuarioSession.getRol().equalsIgnoreCase("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("Error","Acceso Restringido"));
        }
        //------------------blindo por rol-----------
        if (usuarioSession.getRol().equalsIgnoreCase("ODONTOLOGO")){
            // me aseguro que el objeto odontologo tenga el id del odontologo logueado
            if(request.getOdontologo() == null){
                request.setOdontologo(new Usuario());
            }
            request.getOdontologo().setId(usuarioSession.getId());
        }
        /// -----------------------------------------------------si no entra en el if de odontologo es por que es admin
        return ResponseEntity.ok(citaService.disponibilidadOdontologo(request.getOdontologo(),request.getFecha()));

    }


    @PostMapping("/paciente/consultarcita")
    public ResponseEntity<?>consultarcitapb(@RequestBody ConsultaCitaRequestDTO request){ // usa el dto para incluir el odontologo y la fecha todo dentro del body
       return ResponseEntity.ok(citaService.versionpaciente(request.getOdontologo(),request.getFecha())); // aqui en vez de meter odontologo y fecha como parametro en la funcion mete lo que pasa por el request para tener el odontologo y la fecha dentro del body usando ese dto
    }

    @GetMapping("/listarodontologos")
    public ResponseEntity<?>litarodontologos(){
        return ResponseEntity.ok(citaService.listarodontologos());
    }

    @PostMapping("/citaspaciente")
    public ResponseEntity<?> citaspaciente(HttpSession session) {
        // 1. Verificamos la sesión activa
        RespuestaAlLoguaarseDTO usuarioSession = (RespuestaAlLoguaarseDTO) session.getAttribute("usuarioLogueado");
        if(usuarioSession == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No hay sesión activa"));
        }

        // 2. Validamos que el rol sea PACIENTE (siguiendo tu línea de blindaje por rol)
        if(!usuarioSession.getRol().equalsIgnoreCase("PACIENTE") && !usuarioSession.getRol().equalsIgnoreCase("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso Restringido"));
        }

        // 3. Obtenemos las citas usando el ID del paciente logueado en la sesión
        return ResponseEntity.ok(citaService.consultarcitaspropiaspaciente(usuarioSession.getId()));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadocita(@PathVariable Long id,@RequestBody Map<String,String> body,HttpSession session){
        RespuestaAlLoguaarseDTO usuarioSession = (RespuestaAlLoguaarseDTO) session.getAttribute("usuarioLogueado");
        if(usuarioSession == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","no hay sesion activa"));
        }
        //valido roles permitidos para mover los estados (solo si eres odontologo o admin)
        if (!usuarioSession.getRol().equalsIgnoreCase("ODONTOLOGO")&& !usuarioSession.getRol().equalsIgnoreCase("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Acceso Restringido"));
        }
        try{
            String nuevoEstado = body.get("estado");
            Cita citaActualizada = citaService.actualizarestado(id, nuevoEstado,usuarioSession);
            return ResponseEntity.ok(citaActualizada);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));

        }
    }



}
