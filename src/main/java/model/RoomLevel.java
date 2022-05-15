package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room_levels")
public class RoomLevel {

  @Id
  private int id;
  private String name;

  @OneToMany(mappedBy = "roomLevel")
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


  public Set<RoomCategory> getRoomCategories() {
    return roomCategories;
  }

  public void setRooms(Set<RoomCategory> roomCategories) {
    this.roomCategories = roomCategories;
  }

}
