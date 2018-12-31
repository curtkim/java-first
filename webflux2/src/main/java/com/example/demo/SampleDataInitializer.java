package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@Component
@org.springframework.context.annotation.Profile("demo")
class SampleDataInitializer
    implements ApplicationListener<ApplicationReadyEvent> {

  private final ProfileRepository repository;

  public SampleDataInitializer(ProfileRepository repository) {
    this.repository = repository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    repository
        .deleteAll()
        .thenMany(
            Flux
                .just("A", "B", "C", "D")
                .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com"))
                .flatMap(repository::save)
        )
        .thenMany(repository.findAll())
        .subscribe(profile -> log.info("saving " + profile.toString()));
  }
}