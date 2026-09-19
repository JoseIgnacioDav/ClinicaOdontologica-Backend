// Paso 3
/*Este archivo será el puente directo entre tu lógica de Java y la base de datos PostgreSQL en Docker.
 Al tratarse de una interfaz de Spring Data JPA, no necesitamos escribir consultas SQL manuales
 para guardar o buscar usuarios.*/

package com.clinicaodontologica.backend.repository;
import com.clinicaodontologica.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// Debe extender de Jparepository ya que de aqui heredara el comportamiento
// Spring data JPA lee automaticamente <la entidad (la tabla) con la que va a trabajar el repositorio. y el tipo
// de dato que utiliza la clave primaria que definimos en la clase Usuario... que fue Long..>

// TODO nota mia: (Gracias a esto, Spring Boot sabe qué tipo de dato debe recibir y
//  devolver de forma automática en los métodos del repositorio, como findById(Long id)
//  o deleteById(Long id).
//  Si en lugar de un id numérico hubieras definido tu clave primaria como un texto (String), ahí tendrías que poner String.
//  -Como tu id es un Long, le indicas Long para que coincidan perfectamente.)

// con estos dos datos usuario y log ya genera por debajo el crud   save findall findbyid delete sin tener
// que escribir sql a mano

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    // se pone optional para que el usuario pueda existir o no plan al intentar loguarse si no se ha registrado
    //evita los nullpointers, si lo encuentra te lo da dentro de la caja , si no  la caja
    // viene vacia y el codigo no se rompe
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCedula(String cedula);

}
