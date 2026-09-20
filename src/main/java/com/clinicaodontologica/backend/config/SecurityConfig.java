package com.clinicaodontologica.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // esta anotacion le dice a spring que esta clase contiene configuaciones globales del sistema
public class SecurityConfig {
    /*Un Bean en Spring no es más que un objeto de Java que es creado, configurado y administrado por el propio Spring Boot.
Para entenderlo fácil con una analogía:
Imagina que Spring es el gerente de una bodega gigante de herramientas.
En el Java tradicional, si necesitas una herramienta (como el encriptador de contraseñas), te toca ir a fabricarla tú mismo cada vez escribiendo new BCryptPasswordEncoder().
Con un Bean, tú le dices a Spring: "Oye, fabrica una sola vez esta herramienta, guárdala en tu bodega, y cuando cualquier otra parte de mi código la necesite, simplemente dásela".*/
    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(); // Esta es la herramienta matemática oficial de BCrypt
    }
    //por defecto al ponerle security en el pom esta cosa se pone en modo caja fuerte si le mandas post sin la clave
    // no te deja pasar los datos y te devuelve un 401 unauthorized asi que le vamos a crear un bean para que permita la entrada
    // solamente a las rutas /api/auth/register y  /api/auth/login  les dice que son publicas y dejalas entrar
    // cualquier otra ruta sigue pidiendo autenticarse

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //desactivaremos csrf cross site request forgery por que estamos creando una API REST
        // que sera consumida por clientes externos a Postman, no por formularios tradicionales de servidor

        // 2. Configuramos las reglas de acceso por URL //  .authorizeHttpRequests(auth -> auth
        // Permitimos el acceso libre y sin restricciones a los endpoints de registro y login .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
        //// Cualquier otra petición que llegue al sistema exigirá que el usuario esté autenticado .anyRequest().authenticated()
        http.csrf(csrf ->csrf.disable()).
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register","/api/auth/login","/citas/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        return http.build();

        // prueba a ver si esto se puede regresar a una version anterior
    }

}
