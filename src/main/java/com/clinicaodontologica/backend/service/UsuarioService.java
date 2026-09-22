//Paso 4
/*
Piensa en esta clase como el cerebro de la aplicación.
Aquí es donde vive la lógica de negocio pura: validar reglas antes de guardar, verificar si un correo ya existe
y coordinar con el repositorio. Ni la base de datos ni los controladores toman decisiones; todo se procesa aquí.
*/

//dentro tendremos:
//1. La anotación de Servicio y la Inyección del Repositorio
// debemos declarar la clase con anotacion @Service
// y recibir UsuarioRepository a atraves de un constructor

package com.clinicaodontologica.backend.service;

import com.clinicaodontologica.backend.dto.response.usuario.RespuestaAlLoguaarseDTO;
import com.clinicaodontologica.backend.dto.response.usuario.RespuestaAlcrearUsuarioDTO;
import com.clinicaodontologica.backend.model.Usuario;
import com.clinicaodontologica.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository; // {TODO}->    se pone final (osea constante) para que no pueda ser modificado, aqui estamos declarando un tipo de dato UsuarioRepository la interfaz que conecta con la bdd
    private final PasswordEncoder passwordEncoder; //{TODO SECURITY}  declaramos la herramienta aqui
    //{TODO SECURITY} 2. Modificamos el constructor para que Spring te inyecte ambos automáticamente
    // le decimos a spring que inyecte la dependencia de passwordencoder y lo metemos en el constructor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){ // creamos un constructor y le vamos a inyectar la dependencia de la interfaz repositorio que tiene esos metodos de jpa
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RespuestaAlcrearUsuarioDTO registrarUsuario(Usuario usuariorecibido)  {// ahora las excepciones se manejan en la carpeta exception

        // busco el correo y la cedula que recibo del usuariorecibido  en la bdd
        Optional usuariollamadobddcdla = usuarioRepository.findByCedula(usuariorecibido.getCedula());
        Optional usuariollamadobddemail = usuarioRepository.findByEmail(usuariorecibido.getEmail());

        if(usuariollamadobddemail.isPresent() || usuariollamadobddcdla.isPresent()){
            throw new RuntimeException("No se puede registrar este usuario porque el email o cédula ya están registrados.");
        }
        // si no hay error guardamos el usuario que nos pasaron a la base de datos y retornamos el usuario Guardado
        //{todo} esto de abajo se manejaba asi directo guardando la clave en texto plano
       // return usuarioRepository.save(usuariorecibido); // retornamos el usuario que se guardo para que el controller reciba el dato
        //{todo} ahora vamos a cambiar lo del save para que guarde cifrado la password en la bdd
        String passwordEntextoplano = usuariorecibido.getContrasena(); // la sacamos en texto plano
        // la encripatmos usando el beam de bcrypt (esto la convierte un hash ilegible)
        String passwordSeguro = passwordEncoder.encode(passwordEntextoplano);
        // ahora en vez de usar get contrasena vamos a usar set contrasena para reemplazarlo por el hash antes de guardarlo
        usuariorecibido.setContrasena(passwordSeguro);
        //------------------------------------------------------
        // ahora usaremos el dto para devolcer el usuario limpio sin informacion extra
        usuarioRepository.save(usuariorecibido);
        RespuestaAlcrearUsuarioDTO usuariolimpio = new RespuestaAlcrearUsuarioDTO();
        usuariolimpio.setId(usuariorecibido.getId());
        usuariolimpio.setNombres(usuariorecibido.getNombres());
        usuariolimpio.setApellidos(usuariorecibido.getApellidos());
        usuariolimpio.setCedula(usuariorecibido.getCedula());
        usuariolimpio.setEmail(usuariorecibido.getEmail());
        usuariolimpio.setRol(usuariorecibido.getRol());

        return usuariolimpio;

    }

    public RespuestaAlLoguaarseDTO login(String email,String password) {
        //ponemos <usuario> para que sea un dato opcional de tipo usuario ya abajo te sirve para reconvertirlo
        // si no esta vacio sino
        //pierde el rastro de que era originalmente.

        Optional <Usuario> datosdelusuarioalqueintentaentrar = usuarioRepository.findByEmail(email);
        // reviso si el usuario siquiera existe  en la bdd con el correo que comprobamos
        if (!datosdelusuarioalqueintentaentrar.isPresent()){
            throw new RuntimeException("Ese correo no esta registrado");
        }
        // si existe toca sacarlo de optional por que como si existe lo pongo en su clase original asi no da error
        Usuario eldatooptionalenmodousuario = datosdelusuarioalqueintentaentrar.get();
        //------------------------------------------------------------------------------
        /* todo este es el codigo que ejeuctabamos antes pero como ahora estamos usando bcrypt ya no usaremos equals usaremos matches
        if (!eldatooptionalenmodousuario.getPassword().equals(password)){
            throw new Exception("Lo siento la contrasena es incorrecta");
        }*/
        //si todo sale bien devuelve el usuario
       // return eldatooptionalenmodousuario;//
        //--------------------------------------------
        // codigo nuevo:
        if(!passwordEncoder.matches(password, eldatooptionalenmodousuario.getContrasena())){
            throw new RuntimeException("Lo siento, la contasena es incorrecta");
        }
        // return eldatooptionalenmodousuario; esto antes devolvia el usuario al front pero incluia la contrasena cifrada pero igual es una clave que no necesita andar aqui
        //si todo sale bien devuelve el dto limpio con los datos del usuario para el front
        RespuestaAlLoguaarseDTO usuariodatoslimitados = new RespuestaAlLoguaarseDTO();
        usuariodatoslimitados.setId(eldatooptionalenmodousuario.getId());
        usuariodatoslimitados.setNombres(eldatooptionalenmodousuario.getNombres());
        usuariodatoslimitados.setApellidos(eldatooptionalenmodousuario.getApellidos());
        usuariodatoslimitados.setEmail(eldatooptionalenmodousuario.getEmail());
        usuariodatoslimitados.setCedula(eldatooptionalenmodousuario.getCedula());
        usuariodatoslimitados.setRol(eldatooptionalenmodousuario.getRol());

        return usuariodatoslimitados; // ya no retorna la contrasena al front
    }

}
