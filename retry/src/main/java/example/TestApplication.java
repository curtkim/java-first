package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @Autowired
  private MyService myService;

  @Autowired
  private MyService2 myService2;

  @Override
  public void run(String... args) throws Exception {
    try {
      System.out.println(myService.get());
    }
    catch (Exception ex){
      System.out.println("ignore");
      // ignore
    }

    System.out.println("myService2");
    System.out.println(myService2.get());
  }
}
