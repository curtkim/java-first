package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication()
public class EchoServer {

  @RestController
  class EchoController {

    @RequestMapping("/")
    public String echo(@RequestParam("msg") String msg, @RequestParam("delay") long delay) {
      try {
        Thread.currentThread().sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return msg;
    }

  }

  public static void main(String[] args) {
    SpringApplication.run(EchoServer.class, args);
  }
}
