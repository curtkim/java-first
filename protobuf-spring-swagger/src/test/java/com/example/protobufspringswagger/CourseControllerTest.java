package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CourseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourseRepository courseRepository;

  @Test
  public void testGetOneJson() throws Exception {
    when(courseRepository.getCourse(1))
        .thenReturn(BaeldungTraining.Course.newBuilder().setCourseName("REST with Spring").build());

    mockMvc.perform(
            get("/courses/1")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("REST with Spring")));
  }

  @Test
  public void testGetOneProtobuf() throws Exception {
    BaeldungTraining.Course course = BaeldungTraining.Course.newBuilder().setCourseName("REST with Spring").build();
    when(courseRepository.getCourse(1))
        .thenReturn(course);

    mockMvc.perform(
            get("/courses/1")
                .accept("application/x-protobuf")
        )
        .andExpect(status().isOk())
        .andExpect(content().bytes(course.toByteArray()));
  }


  @Test
  public void testGetListJson() throws Exception {
    when(courseRepository.getAll())
        .thenReturn(Arrays.asList(
            BaeldungTraining.Course.newBuilder().setCourseName("REST with Spring").build(),
            BaeldungTraining.Course.newBuilder().setCourseName("REST with Spring2").build()
            ));

    mockMvc.perform(
            get("/courses/")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("REST with Spring")))
        .andExpect(content().string(containsString("REST with Spring2")));
  }
}
