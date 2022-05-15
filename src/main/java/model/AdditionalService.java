package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "additional_services")
public class AdditionalService {

  @Id
  private int id;
  private String name;
  @Column(name = "description", columnDefinition = "MEDIUMTEXT")
  private String description;
  @Column(name = "price", columnDefinition = "DECIMAL")
  private double price;
  private String currency;
  @Column(name = "time_start")
  private java.sql.Time timeStart;
  @Column(name = "time_end")
  private java.sql.Time timeEnd;

  @ManyToMany(mappedBy = "additionalServices")
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


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public java.sql.Time getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(java.sql.Time timeStart) {
    this.timeStart = timeStart;
  }


  public java.sql.Time getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(java.sql.Time timeEnd) {
    this.timeEnd = timeEnd;
  }


  public Set<RoomCategory> getRoomCategories() {
    return roomCategories;
  }

  public void setRoomCategories(Set<RoomCategory> roomCategories) {
    this.roomCategories = roomCategories;
  }

}
