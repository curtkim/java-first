package tutorial;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tutorial.dao.PetRepository;
import tutorial.domain.Pet;

@RestController
@RequestMapping("/pet")
public class PetController {

  @Autowired private PetRepository repository;
  @Autowired private KafkaTemplate<String, String> kafkaTemplate;


  @RequestMapping(value = "/", method = RequestMethod.GET)
  public List<Pet> getAllPets() {
    return repository.findAll();
  }

  @RequestMapping(value = "/box", method = RequestMethod.GET)
  public List<Pet> findByLocation(
      @RequestParam final double minX,
      @RequestParam final double minY,
      @RequestParam final double maxX,
      @RequestParam final double maxY) {
    Box box = new Box(new Point(minX, minY), new Point(maxX, maxY));
    return repository.findByLocationWithin(box);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<Pet> getPetById(@PathVariable("id") String id) {
    // https://stackoverflow.com/questions/2066946/trigger-404-in-spring-mvc-controller
    return repository
        .findById(id)
        .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void modifyPetById(@PathVariable("id") String id, @Valid @RequestBody Pet pet) {
    pet.setId(id);
    repository.save(pet);
    kafkaTemplate.send(KafkaListenerBean.TOPIC, id, pet.toString());
  }
}