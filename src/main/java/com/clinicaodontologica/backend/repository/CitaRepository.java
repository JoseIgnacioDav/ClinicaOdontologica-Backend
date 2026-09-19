package com.clinicaodontologica.backend.repository;

import com.clinicaodontologica.backend.model.Cita2;
import com.clinicaodontologica.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface CitaRepository extends JpaRepository<Cita2,Long> {
    List<Cita2> findByOdontologoAndFecha(Usuario odontologo, LocalDate fecha); // estp le dice a la tabla Citas "Dame todas las citas donde el odontólogo sea X y la fecha sea Y"
}
