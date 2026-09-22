package com.clinicaodontologica.backend.repository;

import com.clinicaodontologica.backend.model.Cita;
import com.clinicaodontologica.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface CitaRepository extends JpaRepository<Cita,Long> {
    List<Cita> findByOdontologoAndFecha(Usuario odontologo, LocalDate fecha); // estp le dice a la tabla Citas "Dame todas las citas donde el odontólogo sea X y la fecha sea Y"

    List<Cita> findCitasByPaciente_Id(Long pacienteId);
}
