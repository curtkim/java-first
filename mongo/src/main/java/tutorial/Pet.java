package tutorial;

import org.springframework.data.annotation.Id;

public class Pet {
  @Id
  public String id;

  public String name;
  public String species;
  public String breed;
  public double footSize;


  // Constructors
  public Pet() {}

  public Pet(String id, String name, String species, String breed, double footSize) {
    this.id = id;
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.footSize = footSize;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getSpecies() { return species; }
  public void setSpecies(String species) { this.species = species; }

  public String getBreed() { return breed; }
  public void setBreed(String breed) { this.breed = breed; }

  public double getFootSize() {
    return footSize;
  }

  public void setFootSize(double footSize) {
    this.footSize = footSize;
  }
}
