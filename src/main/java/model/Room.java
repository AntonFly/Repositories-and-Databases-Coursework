package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {

  @Id
  private int id;
  @Column(name = "room_category_id", nullable = false)
  private int roomCategoryId;
  private int number;
  @Column(name = "hotel_id", nullable = false)
  private int hotelId;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getRoomCategoryId() {
    return roomCategoryId;
  }

  public void setRoomCategoryId(int roomCategoryId) {
    this.roomCategoryId = roomCategoryId;
  }


  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }


  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }

}
