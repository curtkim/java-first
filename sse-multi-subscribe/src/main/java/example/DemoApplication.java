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
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Consumer;

import static java.time.Duration.ofSeconds;

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
    Consumer<? super FluxSink<Long>> emitter = (fluxSink) ->{
      while(true) {
        fluxSink.next(System.currentTimeMillis());
      }
    };

    ConnectableFlux<Long> publish = Flux.create(emitter, FluxSink.OverflowStrategy.DROP)
      .sample(ofSeconds(1))
      .subscribeOn(Schedulers.elastic())
      .publish();

    Flux<Long> flux = publish.autoConnect(1);

    Flux<String> get(int i) {
      return flux.filter(v-> v % i == 0).map(v -> v.toString()).log();
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
      eventService.get(2).subscribe((v)->{
        System.out.println( Thread.currentThread().getName() + " : " + v);
      });
    };
  }
}
