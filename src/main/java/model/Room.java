package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {

  @Id
  private int id;

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "room_category_id")
  private RoomCategory roomCategory = new RoomCategory();

  private int number;

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "hotel_id")
  private Hotel hotel = new Hotel();

  @OneToMany(mappedBy = "room")
  private Set<Order> orders = new HashSet<>();


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public RoomCategory getRoomCategory() {
    return roomCategory;
  }

  public void setRoomCategory(RoomCategory roomCategory) {
    this.roomCategory = roomCategory;
  }


  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }


  public Hotel getHotel() {
    return hotel;
  }

  public void setHotelId(Hotel hotel) {
    this.hotel = hotel;
  }


  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }

}
