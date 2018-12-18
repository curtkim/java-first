package tutorial.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.geo.Box;
import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.domain.Pet;

public interface PetRepository extends MongoRepository<Pet, String> {
  Optional<Pet> findById(String id);
  List<Pet> findByLocationWithin(Box box);
}