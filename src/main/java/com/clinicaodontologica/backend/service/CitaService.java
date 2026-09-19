package com.clinicaodontologica.backend.service;

import com.clinicaodontologica.backend.model.Cita2;
import com.clinicaodontologica.backend.model.Usuario;
import com.clinicaodontologica.backend.repository.CitaRepository;
import com.clinicaodontologica.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CitaService(CitaRepository citaRepository,UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.citaRepository = citaRepository;
        this.usuarioRepository= usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cita2 crearcita (Cita2 citaqueintentancrear)throws Exception{
        // esta madre esta inseguro luego me toca hacer lo del token por que sino esta full atacable solo es cuestion de meterse a esta ruta e inyectar datos
        //llamamos al usuario que trata de crear la cita por email
        Optional<Usuario> usuarioquetratadecrearlacita = usuarioRepository.findByEmail(citaqueintentancrear.getEmailqueintentacrearlacita());
        if (usuarioquetratadecrearlacita.isEmpty()){
            throw new Exception("permiso denegado");
        }
        //vamos a comprobar si metio bien la clave  primero lo pasamos a modo usuario por q sabemos que no esta vacio
        Usuario usuarioquetratadecrearlacitamodousuario = usuarioquetratadecrearlacita.get();
        if (!passwordEncoder.matches(citaqueintentancrear.getPassworddelmailquetratadecrearlacita(),usuarioquetratadecrearlacitamodousuario.getContrasena())){
            throw new Exception("permiso denegado");
        }
        // si el usuario si existe y metio bien la clave, lo vamos a guardar pero primero tenemos que cifrar la clave de la cita
        String passwordcitatextoplano = citaqueintentancrear.getPassworddelmailquetratadecrearlacita();
        String clavecifrafadelacita = passwordEncoder.encode(passwordcitatextoplano);
        citaqueintentancrear.setPassworddelmailquetratadecrearlacita(clavecifrafadelacita);
        citaRepository.save(citaqueintentancrear);
        return citaqueintentancrear;
    }

    public List<Cita2>disponibilidadOdontologo(Usuario odontologo,LocalDate fecha){ // esto busca las citas que tiene el odontologo en esa fecha
       return citaRepository.findByOdontologoAndFecha(odontologo,fecha);
    }

}
