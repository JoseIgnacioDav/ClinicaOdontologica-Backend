package com.clinicaodontologica.backend.controller;

import com.clinicaodontologica.backend.dto.response.usuario.RespuestaAlLoguaarseDTO;
import com.clinicaodontologica.backend.dto.response.usuario.RespuestaAlcrearUsuarioDTO;
import com.clinicaodontologica.backend.model.Usuario;
import com.clinicaodontologica.backend.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
// le dice que esta clase es un restcontroller a spring
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth") // todos los endpoints partiran de esta ruta
public class UsuarioController {
private final UsuarioService usuarioService; // declaro la dependencia

public UsuarioController(UsuarioService usuarioService){
    this.usuarioService = usuarioService;     // le digo a spring que inyecte la dependencia
}


//   {TODO}    el endpoint de registro
    // se ponre <?> en el responsentity por que puede recibir tanto un tipo Usuario o un map con el e.getmessage() que retornara el contenido string de  la excepcion en forma de mapa
    @PostMapping("/register") // le indico la ruta de este endpoint
    public ResponseEntity<?> registrar(@RequestBody Usuario usuarioaqueentraporfront){ // creo el metodo registrar que va a pedir en el body un Usuario que tenga todos los campos que se pide en el model
    try{   // hago try catch por que los metodos de usuarioservice tienen excepciones y queremos que le mande las excepciones tambien por un map al front.. clave error valor: la excepcion que capturo de la funcion de service
        RespuestaAlcrearUsuarioDTO nuevoUsuario = usuarioService.registrarUsuario(usuarioaqueentraporfront); // si no tira excepcion el metodo registrarusuario de usuarioservice se guarda en usuarionuevo
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // y nos devuelve el estatus creado y el body con el usuario creado y se va al front !
    } catch (Exception e){  // si al tratar de jalar registrarUsuario se da una excepcion, catch la captura  y la nombramos excepcion e, puede tener cualquier nombre jajaj (obvio no se ejecuta el metodo registrar !! nos tiro una excepcion jajaj !1)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); // y nos devuelve un status bad request junto con el body que tendra un mapa con el valor "error" y la clave el mensaje de la excepcion e, como es de tipo exception tiene este metodo getmessage bueno esa madre se va al front en un mapa
    }
 }

    // {TODO}      el endpoint del login

    // misma vaina de arriba
    // se ponre <?> en el responsentity por que puede recibir tanto un tipo Usuario o un map con el e.getmessage()
    // que retornara el contenido string de  la excepcion en forma de mapa
    @PostMapping("/login") // la ruta del endpoint del login                             // le pongo la sesion aqui
    public ResponseEntity<?> login(@RequestBody Map<String,String> credencialesporfront, HttpSession session){ // en este metodo le pedimos al front un body con un mapa de dos strings (serian el email y la clave)
    try {
        String email = credencialesporfront.get("email");  // creamos una variable que tomara el valor de la clave que tenga "email"
        String password = credencialesporfront.get("password"); //creamos una variable que tomara el valor de la clave del mapa que contenga  la palabra password
        RespuestaAlLoguaarseDTO usuarioLogueado = usuarioService.login(email,password);
        // si el metodo login  de usuarioService se ejecuta sin excepciones se guarda en usuariologueado
        //-- guardo los datos del usuario loguedo en la sesion del servidor-----
        /*aqui pasa lo siguiente:
        * Crea una sesión nueva para este usuario, guárdale su información y emite la
        * cookie JSESSIONID*/
        session.setAttribute("usuarioLogueado",usuarioLogueado);
        /////
        return ResponseEntity.ok(usuarioLogueado); // nos devuelve un ok al front y el usuariologueado
    }catch (Exception e){ // si se da una excepcion en el emtodo loginplaintext la captura catch, y obvio no se ejecuta el loginplaintext
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",e.getMessage())); // nos devuelve un status al front con no autorzado y un mapa con el error como clave  y el mensaje con la excepcion igual que arriba
    }
    }




}