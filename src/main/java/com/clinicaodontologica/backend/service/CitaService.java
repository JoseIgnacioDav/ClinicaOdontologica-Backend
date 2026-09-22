package com.clinicaodontologica.backend.service;

import com.clinicaodontologica.backend.dto.response.cita.ConsultarcitasagendadaspacienteDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConfirmacionCreacionCitaPAcienteDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConsultarCitasLibresOdontologoPacientePublicDTO;
import com.clinicaodontologica.backend.dto.response.cita.ConsultarCitasOdontologoConfidentialResponseDTO;
import com.clinicaodontologica.backend.dto.response.cita.ListarOdontologosDTO;
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
    // agrego la sesion como uno de los parametros que necesita para crear la cita
    public ConfirmacionCreacionCitaPAcienteDTO crearcita (Cita citaqueintentancrear,Long pacienteIdsesion){// la excepcion ahora se maneja con la carpeta exception{
        // busco al paciente en la bdd usando el id seguro
        Optional<Usuario> usuariologueado = usuarioRepository.findById(pacienteIdsesion);
        //verifico si existe
        if(usuariologueado.isEmpty()){
            throw new RuntimeException("usuario no encontrado");
        }
        // si pasa el if ya sabemos que no esta vacio entonces lo convertimos en usuario para acceder al rol
        Usuario usuarioLogueadomodousuario = usuariologueado.get();
        // Evaluamos quien crea la cita por el rol
        if(usuarioLogueadomodousuario.getRol().equalsIgnoreCase("PACIENTE")){
            // si es paciente ignoramos culaquier cosa del json
            // y le asignamos su propio id de la sesion
            citaqueintentancrear.setPaciente(usuarioLogueadomodousuario);
        } else if (usuarioLogueadomodousuario.getRol().equalsIgnoreCase("ODONTOLOGO") || usuarioLogueadomodousuario.getRol().equals("ADMIN")) {
            // si el rol es odontologo o admun se permite que se traiga el id del paciente al que se le va a agendar la cita
            if (citaqueintentancrear.getPaciente() == null || citaqueintentancrear.getPaciente().getId() == null) {
                throw new RuntimeException("Debe especificar el id del paciente para esta cita");
            }
            Optional<Usuario> pacienteOPTDelJson = usuarioRepository.findById(citaqueintentancrear.getPaciente().getId());
            // checamos si el optional esta vacio
            if (pacienteOPTDelJson.isEmpty()) {
                throw new RuntimeException("El paciente indicado no existe en la bdd");
            }
            // si pasa el if lo saco de optional y se lo pongo a un usuaario
            Usuario pacienteDelJson = pacienteOPTDelJson.get();
            citaqueintentancrear.setPaciente(pacienteDelJson);
        }
        // guardo la cita en la bdd
            citaRepository.save(citaqueintentancrear);
            // devuelvo con un dto para no exponer datos
            ConfirmacionCreacionCitaPAcienteDTO citaqueintentancrearsindatosexpuestos = new ConfirmacionCreacionCitaPAcienteDTO();
            citaqueintentancrearsindatosexpuestos.setId(citaqueintentancrear.getId());
            citaqueintentancrearsindatosexpuestos.setEstado(citaqueintentancrear.getEstado());
            citaqueintentancrearsindatosexpuestos.setHora(citaqueintentancrear.getHora());
            citaqueintentancrearsindatosexpuestos.setFecha(citaqueintentancrear.getFecha());
            // llenado de nombres para la respuesta que va por front
            citaqueintentancrearsindatosexpuestos.setNombrepaciente(citaqueintentancrear.getPaciente().getNombres() + " " + citaqueintentancrear.getPaciente().getApellidos());
            Usuario odontologoqueatenderalacita = usuarioRepository.findById(citaqueintentancrear.getOdontologo().getId()).orElse(null);
            if (odontologoqueatenderalacita != null){
                citaqueintentancrearsindatosexpuestos.setNombreodontologo(odontologoqueatenderalacita.getNombres()+" "+ odontologoqueatenderalacita.getApellidos());
            }
            return citaqueintentancrearsindatosexpuestos;


            // la logica anterior pedia email y contrasena para validar la creacion de una cita  ahora se usa el sesion id y el rol

    }
    //logica del endpoint para devolver citas agendadas del paciente
    public List<ConsultarcitasagendadaspacienteDTO>consultarcitaspropiaspaciente (Long id){
        List<Cita> citasexpuestas = citaRepository.findCitasByPaciente_Id(id);
        List<ConsultarcitasagendadaspacienteDTO> citaslimpias = new ArrayList<>();
        for (Cita iteracioncitaexpuesta : citasexpuestas){
            ConsultarcitasagendadaspacienteDTO citalimpiauxiliar = new ConsultarcitasagendadaspacienteDTO();
            citalimpiauxiliar.setOdontologo(iteracioncitaexpuesta.getOdontologo().getNombres()+" "+ iteracioncitaexpuesta.getOdontologo().getApellidos());
            citalimpiauxiliar.setHora(iteracioncitaexpuesta.getHora());
            citalimpiauxiliar.setId(iteracioncitaexpuesta.getId());
            citalimpiauxiliar.setFecha(iteracioncitaexpuesta.getFecha());
            citalimpiauxiliar.setEstado(iteracioncitaexpuesta.getEstado());
            citaslimpias.add(citalimpiauxiliar);
        }
        return citaslimpias;
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


    public List<ListarOdontologosDTO>listarodontologos(){
        List<ListarOdontologosDTO> listaodontologoslimipia = new ArrayList<>();
        String rol = "ODONTOLOGO";
        List<Usuario> listaodontologos = usuarioRepository.findByRolIgnoreCase(rol);
        if(listaodontologos.isEmpty()){
            throw new RuntimeException("No hay odontologos");
        }
        for (Usuario iteracionusuario : listaodontologos){
            ListarOdontologosDTO odontologoidynombre = new ListarOdontologosDTO();
            odontologoidynombre.setId(iteracionusuario.getId());
            odontologoidynombre.setNombres(iteracionusuario.getNombres()+" "+ iteracionusuario.getApellidos());
            listaodontologoslimipia.add(odontologoidynombre);
        }
        return listaodontologoslimipia;
    }

}
