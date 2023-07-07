package com.example;

import com.example.domain.MyRecord;
import com.example.domain.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class LettuceSpringNoTemplateApplication implements CommandLineRunner {

  public static void main(String[] args) {

    SpringApplicationBuilder builder = new SpringApplicationBuilder()
        .sources(LettuceSpringNoTemplateApplication.class)
        .bannerMode(Banner.Mode.OFF);

    SpringApplication app = builder.build();
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args).close();
  }

  @Autowired
  MyService myService;

  @Override
  public void run(String... args) {
    myService.update("a", "summarya", Arrays.asList("slice1", "slice2"));
    MyRecord rec = myService.get("a");
    System.out.println(rec);
  }

  /*
  @Autowired
  LettuceTemplate lettuceTemplate;

  @Override
  public void run(String... args) {

    lettuceTemplate.execute("key", conn -> {
      RedisCommands<String, String> commands = conn.sync();
      commands.set("key", "value2");

      String value = get("key");
      System.out.println(value);
      return null;
    });
  }

  String get(String key){
    return (String)lettuceTemplate.execute(key, conn ->{
      RedisCommands<String, String> commands = conn.sync();
      return commands.get(key);
    });
  }
   */
}
