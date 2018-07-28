package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {

  @RestController
  @RequestMapping("/event")
  class EventController {

    @Autowired
    EventService eventService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribe(@RequestParam("modulus") int modulus){
      return eventService.get(modulus);
    }
  }


  @Service
  class EventService {
    private Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)).log();

    Flux<String> get(int i) {
      return interval.filter(v-> v % i == 0).map(v -> v.toString());
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }


  @Autowired
  EventService eventService;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      eventService.get(2).subscribe(System.out::println);
    };
  }
}
