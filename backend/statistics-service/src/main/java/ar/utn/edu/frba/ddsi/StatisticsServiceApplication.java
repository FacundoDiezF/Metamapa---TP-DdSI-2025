package ar.utn.edu.frba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StatisticsServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(StatisticsServiceApplication.class, args);
  }
}