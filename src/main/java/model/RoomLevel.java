package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room_levels")
public class RoomLevel {

  @Id
  private int id;
  private String name;


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

}
