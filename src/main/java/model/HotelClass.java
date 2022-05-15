package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotel_class")
public class HotelClass {

  @Id
  private int id;
  @Column(name = "name", columnDefinition = "MEDIUMTEXT")
  private String name;

  @OneToMany(mappedBy = "hotelClass")
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


  public Set<Hotel> getHotels() {
    return hotels;
  }

  public void setHotels(Set<Hotel> hotels) {
    this.hotels = hotels;
  }

}
