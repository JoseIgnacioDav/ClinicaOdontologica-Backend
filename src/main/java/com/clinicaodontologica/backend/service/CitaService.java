package com.clinicaodontologica.backend.service;

import com.clinicaodontologica.backend.dto.response.cita.ConfirmacionCreacionCitaPAcienteDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConsultarCitasLibresOdontologoPacientePublicDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConsultarCitasOdontologoConfidentialResponseDTO;
import com.clinicaodontologica.backend.model.Cita;
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

    public ConfirmacionCreacionCitaPAcienteDTO crearcita (Cita citaqueintentancrear)throws Exception{
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

        ConfirmacionCreacionCitaPAcienteDTO citaqueintetancrearsindatosexpuestos = new ConfirmacionCreacionCitaPAcienteDTO();
        citaqueintetancrearsindatosexpuestos.setId(citaqueintentancrear.getId());
        citaqueintetancrearsindatosexpuestos.setEstado(citaqueintentancrear.getEstado());
        citaqueintetancrearsindatosexpuestos.setHora(citaqueintentancrear.getHora());
        citaqueintetancrearsindatosexpuestos.setFecha(citaqueintentancrear.getFecha());
        //{todo}   al momento de crear la cita se le pasa el id como unico parametro conectado con usuario,
        //{todo} por eso jackson crea un usuario por debajo solo con el id, todo lo demas va null, si despues le pido que obtenga esos datos de paciente como no fueron puestso en el post, no los tiene y si los llamo asi va a dar error
        // {todo}   citaqueintetancrearsindatosexpuestos.setNombrepaciente(citaqueintentancrear.getPaciente().getNombres() + " " +citaqueintentancrear.getPaciente().getApellidos()); // no tiene los datos del paciente por que no se los meti en el post solo el id
        // por eso toca crear un usuario para que si tenga los datos llamados por la bdd., te preguntaras por que no reciclaste
        // el que ya tenias arriba llamado: {todo}  [usuarioquetratadecrearlacitamodousuario] que pasa si el que trata de crear la cita no es el mismo que el que va a recibir la cita?  ( como un odontologo o una recepcionista)
        // por eso toca llamar al usuario desde la bdd con el id que si se paso por el post, tampoco hay los datos del odontologo por la misma razon
        Usuario pacientequerecibiralacita = usuarioRepository.findById(citaqueintentancrear.getPaciente().getId()).orElse(null);
        Usuario odontologoqueatenderalacita = usuarioRepository.findById(citaqueintentancrear.getOdontologo().getId()).orElse(null);
        if (pacientequerecibiralacita != null){
            citaqueintetancrearsindatosexpuestos.setNombrepaciente(pacientequerecibiralacita.getNombres() + " " + pacientequerecibiralacita.getApellidos());
        }
        if (odontologoqueatenderalacita != null){
            citaqueintetancrearsindatosexpuestos.setNombreodontologo(odontologoqueatenderalacita.getNombres() + " " + odontologoqueatenderalacita.getApellidos());
        }
        return citaqueintetancrearsindatosexpuestos;
    }


    public List<ConsultarCitasOdontologoConfidentialResponseDTO> disponibilidadOdontologo(Usuario odontologo, LocalDate fecha){
        //buscamos las citas en la bdd, vienen cargadas con contrasenas usuarios e informacion sensible
        List<Cita> citaEntidades = citaRepository.findByOdontologoAndFecha(odontologo,fecha);

        //Creamos una lista vacia que contendra los datos limpios y seguros
        List<ConsultarCitasOdontologoConfidentialResponseDTO> listaDTOs = new ArrayList<>();

        //recorremos cada cita dentro de la lista con datos sensibles con un for
        for(Cita iteracioncita : citaEntidades){
            ConsultarCitasOdontologoConfidentialResponseDTO dto = new ConsultarCitasOdontologoConfidentialResponseDTO(); //en cada iteracion crearemos un dto que se asignara los datos que si se pueden mostrar de cada dto en la lista de dtos que exponian mucha info
            //pasamos los datos basicos
            dto.setId(iteracioncita.getId());
            dto.setFecha(iteracioncita.getFecha());
            dto.setHora(iteracioncita.getHora());
            dto.setEstado(iteracioncita.getEstado());
            //esto es lo mismo que arriba solo con validacion (por si no hay nombre en paciente u odontologo no se rompa el programa)
            if(iteracioncita.getOdontologo() != null){
                dto.setNombreOdontologo(iteracioncita.getOdontologo().getNombres() + " " +iteracioncita.getOdontologo().getApellidos());
            }else {
                dto.setNombreOdontologo("nombre odontologo sin asignar");
            }
            //ahora el nombre del paciente
            if(iteracioncita.getPaciente() != null){
                dto.setNombrePaciente(iteracioncita.getPaciente().getNombres() + " " + iteracioncita.getPaciente().getApellidos());
            }else {
                dto.setNombrePaciente("nombre paciente sin asignar");
            }
            if (iteracioncita.getPaciente().getEmail()!= null) {// revisa si dentro de la iteracion que es de tipo usuario el atributo email de usuario no esta vacio
                dto.setEmailpaciente(iteracioncita.getPaciente().getEmail());
            }
            if (iteracioncita.getPaciente().getCedula() != null){ // revisa si dentor de la iteracion tipo usuario esta el atributo cedula
                dto.setCedulapaciente(iteracioncita.getPaciente().getCedula());
            }
                // ponemos el dto listo en la lista final
            listaDTOs.add(dto);
        }
        //Retornamos la lista pulida sin informacion sensible expuesta

        return listaDTOs;
    }

    public List<ConsultarCitasLibresOdontologoPacientePublicDTO>versionpaciente (Usuario odontologo, LocalDate fecha){
        //buscamos las citas en la bdd, vienen cargadas con contrasenas usuarios e informacion sensible
        List<Cita> citaEntidades = citaRepository.findByOdontologoAndFecha(odontologo,fecha);
        //Creamos una lista vacia que contendra los datos limpios y seguros
        List<ConsultarCitasLibresOdontologoPacientePublicDTO> listaDTOs = new ArrayList<>();
        // recorremos cada cita dentro de la lista con datos sensibles con un for
        for (Cita iteracioncita : citaEntidades){
            ConsultarCitasLibresOdontologoPacientePublicDTO dto = new ConsultarCitasLibresOdontologoPacientePublicDTO(); //en cada iteracion crearemos un dto que se asignara los datos que si se pueden mostrar de cada dto en la lista de dtos que exponian mucha info
            // pasamos los datos limitados
            dto.setId(iteracioncita.getId());
            dto.setFecha(iteracioncita.getFecha());
            dto.setHora(iteracioncita.getHora());
            dto.setEstado(iteracioncita.getEstado());
            if(iteracioncita.getOdontologo() != null) {
                dto.setNombreOdontologo(iteracioncita.getOdontologo().getNombres() + " " + iteracioncita.getOdontologo().getApellidos());
            }
            listaDTOs.add(dto);
        }

        //retornamos la lista sin exposicion de informacion sensible
        return listaDTOs;
    }

}
