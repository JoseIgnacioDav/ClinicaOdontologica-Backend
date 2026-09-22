package com.clinicaodontologica.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration // esta anotacion le dice a spring que esta clase contiene configuaciones globales del sistema
public class SecurityConfig {

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
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/citas/**",
                                "/*.html",
                                "/*.css",
                                "/js/**",
                                "/"
                        )
                        .permitAll()
                        .anyRequest()
                        .permitAll()
                )
                .formLogin(form -> form.disable())// desactivamos el form de html por defecto
                .httpBasic(basic -> basic.disable());
        return http.build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://127.0.0.1:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Vital para que las cookies de HttpSession fluyan de forma correcta

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}