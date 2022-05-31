package example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootTest
public class ApplicationTest {

  @Autowired
  ApplicationContext context;

  @Autowired
  Environment environment;

  @Autowired
  HomeController homeController;

  @Test
  public void contextLoads() {

    assertThat(homeController).isNotNull();
//    for(String profile : environment.getActiveProfiles())
//      System.out.println(profile);

    for(String name : context.getBeanDefinitionNames())
      System.out.println(name);
  }

}
