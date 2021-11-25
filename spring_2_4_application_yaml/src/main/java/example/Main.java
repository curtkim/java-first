package example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args).close();
  }

  @Value("${app.color}")
  private String color;

  @Override
  public void run(String... args) {
    System.out.println(color);
  }
}