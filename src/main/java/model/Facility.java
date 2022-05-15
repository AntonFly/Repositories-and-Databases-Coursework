package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facilities")
public class Facility {

  @Id
  private int id;
  private String name;
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToMany(mappedBy = "facilities")
  private Set<RoomCategory> roomCategories = new HashSet<>();


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


  public Set<RoomCategory> getRoomCategories() {
    return roomCategories;
  }

  public void setRoomCategories(Set<RoomCategory> roomCategories) {
    this.roomCategories = roomCategories;
  }

}
