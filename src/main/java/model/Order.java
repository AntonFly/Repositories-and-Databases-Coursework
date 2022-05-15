package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  private int id;
  @Column(name = "hotel_id", nullable = false)
  private int hotelId;
  @Column(name = "room_id", nullable = false)
  private int roomId;
  @Column(name = "user_id", nullable = false)
  private int userId;
  @Column(name = "date_start")
  private java.sql.Date dateStart;
  @Column(name = "date_end")
  private java.sql.Date dateEnd;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }


  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }


  public java.sql.Date getDateStart() {
    return dateStart;
  }

  public void setDateStart(java.sql.Date dateStart) {
    this.dateStart = dateStart;
  }


  public java.sql.Date getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(java.sql.Date dateEnd) {
    this.dateEnd = dateEnd;
  }

}
