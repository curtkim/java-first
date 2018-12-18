package tutorial.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pets")
public class Pet {
  @Id
  public String id;

  public String name;
  public String species;
  public String breed;

  @JsonProperty("foot_size")
  public double footSize;

  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private Point location;


  // Constructors
  public Pet() {}

  public Pet(String id, String name, String species, String breed, double footSize, Point location) {
    this.id = id;
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.footSize = footSize;
    this.location = location;
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

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "Pet{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", species='" + species + '\'' +
        ", breed='" + breed + '\'' +
        ", footSize=" + footSize +
        ", location=" + location +
        '}';
  }
}