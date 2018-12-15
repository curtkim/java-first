package tutorial;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {
  @Autowired
  private PetRepository repository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public List<Pet> getAllPets() {
    return repository.findAll();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<Pet> getPetById(@PathVariable("id") String id) {
    // https://stackoverflow.com/questions/2066946/trigger-404-in-spring-mvc-controller
    return repository.findById(id)
        .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void modifyPetById(@PathVariable("id") String id, @Valid @RequestBody Pet pet) {
    pet.setId(id);
    repository.save(pet);
  }

}
