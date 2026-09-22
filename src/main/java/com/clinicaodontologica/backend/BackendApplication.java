package com.clinicaodontologica.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;


@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		//aqui manejo los .env antes de que spring inicie
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(BackendApplication.class, args);
	}

}
