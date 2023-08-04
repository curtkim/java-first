package com.example.apps.web;

import com.example.apps.web.WebApplication;
import com.example.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// CliApplication, Web2Application에 독립적으로 web layer만 test
@WebMvcTest(HomeController.class)
@ContextConfiguration(classes = {WebApplication.class})
public class HomeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  HelloService helloService;


  @Test
  public void test() throws Exception {
    when(helloService.hi()).thenReturn("hi");

    mockMvc.perform(
            get("/")
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("hi")));
  }
}
