package ar.utn.edu.frba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication

@EnableScheduling  //cronjob para importar fuentes
public class StaticSourceApplication {
  public static void main(String[] args) {
    SpringApplication.run(StaticSourceApplication.class, args);
  }
}