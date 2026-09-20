// segundo paso, el primer paso fue poner las credenciales y configuariones en application.properties

package com.clinicaodontologica.backend.model;
import jakarta.persistence.*;

//declaro la clase con entity y table lo nombro usuarios
// @Entity le avisa springboot que esta clase esta mapeada con una tabla de la base de datos
// @Table sirve para ponerle un nombre claro y en plural a la tabla postresql usuarios, si no lo haces toma por defecto
// el nombre de la clase en minusculas
@Entity
@Table(name = "usuarios")
public class Usuario { // nos va a pedir que le pongamos un primary key, (toda tabla necesita una primary key en sql)
    @Id // esto marca la variable este abajo como primary key
    @GeneratedValue (strategy = GenerationType.IDENTITY) // esto automatiza la creacion de los ids de cada nuevo registro
    private Long id; // y debajo el tipo de dato que recibira estas propiedades (cosa de spring)

    //Datos del usuario
    @Column(nullable = false,unique = true)
    private String cedula;
    @Column(nullable = false)
    private String nombres;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String contrasena;

    //por defecto si alguien se registra su rol sera paciente.. ya para administradores con mas permisos tocara modificar
    @Column(nullable = false)
    private String rol = "PACIENTE";

    // siempre se debe poner un constructor vacio para que spring funcione
     //Hibernate exige obligatoriamente un constructor vacío (sin argumentos) para poder instanciar-
    // -la clase por debajo cuando hace consultas.
    public Usuario(){}

    // gettes y setters tambien obligatorios Los getters y setters son los que nos permitirán leer y modificar los datos...
    //-de los atributos desde los servicios y controladores de forma segura.
    // si usara lombok lo hace solito


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    // eliminacion del set rol por que permitia que un usuario se asigne el rol odontologo
}

