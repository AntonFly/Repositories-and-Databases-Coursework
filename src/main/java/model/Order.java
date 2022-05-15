package model;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  private int id;

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "hotel_id")
  private Hotel hotel = new Hotel();

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "room_id")
  private Room room = new Room();

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user = new User();

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


  public Hotel getHotel() {
    return hotel;
  }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
  }


  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
