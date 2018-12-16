package tutorial;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PetController.class)
public class PetControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PetRepository petRepository;

  @Test
  public void givenPet_whenGetPet_thenReturnJsonObject() throws Exception {

    Pet pet = new Pet("1", "Liam", "cat", "siamese", 5.3, new Point(1.1, 2.2));

    given(petRepository.findById(pet.getId())).willReturn(Optional.of(pet));

    mvc.perform(get("/pet/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(pet.getName())))
        .andExpect(jsonPath("$.location.x", is(1.1)))
        .andExpect(jsonPath("$.location.y", is(2.2)));
  }

}
