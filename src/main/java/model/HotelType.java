package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotel_type")
public class HotelType {

  @Id
  private int id;
  private String name;
  @Column(name = "description", columnDefinition = "MEDIUMTEXT")
  private String description;

  @OneToMany(mappedBy = "hotelType")
  private Set<Hotel> hotels = new HashSet<>();


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public Set<Hotel> getHotels() {
    return hotels;
  }

  public void setHotels(Set<Hotel> hotels) {
    this.hotels = hotels;
  }

}
