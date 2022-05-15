package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotel implements Serializable {

  @Id
  private int id;
  @Column(name = "name", columnDefinition = "MEDIUMTEXT")
  private String name;
  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "class_id")
  HotelClass hotelClass = new HotelClass();

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "type_id")
  HotelType hotelType = new HotelType();

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "location_id")
  Direction direction = new Direction();

  private String disposition;
  @Column(name = "description", columnDefinition = "MEDIUMTEXT")
  private String description;

  @OneToMany(mappedBy = "hotel")
  private Set<Room> rooms = new HashSet<>();

  @OneToMany(mappedBy = "hotel")
  private Set<Order> orders = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
          name = "hotels_excursions",
          joinColumns = { @JoinColumn(name = "hotel_id") },
          inverseJoinColumns = { @JoinColumn(name = "excursion_id") }
  )
  Set<Excursion> excursions = new HashSet<>();



  public HotelClass getHotelClass() {
    return hotelClass;
  }

  public void setHotelClass(HotelClass hotelClass) {
    this.hotelClass = hotelClass;
  }


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



  public void setHotelType(HotelType hotelType) {
    this.hotelType = hotelType;
  }

  public HotelType getHotelType() {
    return hotelType;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }


  public String getDisposition() {
    return disposition;
  }

  public void setDisposition(String disposition) {
    this.disposition = disposition;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public Set<Excursion> getExcursions() {
    return excursions;
  }

  public void setExcursions(Set<Excursion> excursions) {
    this.excursions = excursions;
  }


  public Set<Room> getRooms() {
    return rooms;
  }

  public void setRooms(Set<Room> rooms) {
    this.rooms = rooms;
  }


  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }


}
