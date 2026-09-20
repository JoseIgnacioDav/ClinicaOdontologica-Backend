package com.clinicaodontologica.backend.service;

import com.clinicaodontologica.backend.dto.response.ConsultarCitasLibresOdontologoPacientePublicDTO;
import com.clinicaodontologica.backend.dto.response.ConsultarCitasOdontologoConfidentialResponseDTO;
import com.clinicaodontologica.backend.model.Cita2;
import com.clinicaodontologica.backend.model.Usuario;
import com.clinicaodontologica.backend.repository.CitaRepository;
import com.clinicaodontologica.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

//    public List<Cita2>disponibilidadOdontologo(Usuario odontologo,LocalDate fecha){ // esto busca las citas que tiene el odontologo en esa fecha
//       return citaRepository.findByOdontologoAndFecha(odontologo,fecha);
//    }

    public List<ConsultarCitasOdontologoConfidentialResponseDTO> disponibilidadOdontologo(Usuario odontologo, LocalDate fecha){
        //buscamos las citas en la bdd, vienen cargadas con contrasenas usuarios e informacion sensible
        List<Cita2> citaEntidades = citaRepository.findByOdontologoAndFecha(odontologo,fecha);

        //Creamos una lista vacia que contendra los datos limpios y seguros
        List<ConsultarCitasOdontologoConfidentialResponseDTO> listaDTOs = new ArrayList<>();

        //recorremos cada cira dentro de la lista con datos sensibles con un for
        for(Cita2 iteracioncita : citaEntidades){
            ConsultarCitasOdontologoConfidentialResponseDTO dto = new ConsultarCitasOdontologoConfidentialResponseDTO(); //en cada iteracion crearemos un dto que se asignara los datos que si se pueden mostrar de cada dto en la lista de dtos que exponian mucha info
            //pasamos los datos basicos
            dto.setId(iteracioncita.getId());
            dto.setFecha(iteracioncita.getFecha());
            dto.setHora(iteracioncita.getHora());
            dto.setEstado(iteracioncita.getEstado());
            //esto es lo mismo que arriba solo con validacion (por si no hay nombre en paciente u odontologo no se rompa el programa)
            if(iteracioncita.getOdontologo() != null){
                dto.setNombreOdontologo(iteracioncita.getOdontologo().getNombres() + " " +iteracioncita.getOdontologo().getApellidos())  ;
            }else {
                dto.setNombreOdontologo("nombre odontologo sin asignar");
            }
            //ahora el nombre del paciente
            if(iteracioncita.getPaciente() != null){
                dto.setNombrePaciente(iteracioncita.getPaciente().getNombres());
            }else {
                dto.setNombrePaciente("nombre paciente sin asignar");
            }
                // ponemos el dto listo en la lista final
            listaDTOs.add(dto);
        }
        //Retornamos la lista pulida sin informacion sensible expuesta

        return listaDTOs;
    }

    public List<ConsultarCitasLibresOdontologoPacientePublicDTO>versionpaciente (Usuario odontologo, LocalDate fecha){

    }

}
