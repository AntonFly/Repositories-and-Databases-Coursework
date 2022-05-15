package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room_categories")
public class RoomCategory {

  @Id
  private int id;
  private String name;

  @ManyToOne(optional=false, cascade=CascadeType.ALL)
  @JoinColumn(name = "level_id")
  private RoomLevel roomLevel = new RoomLevel();

  private int capacity;
  @Column(name = "price", columnDefinition = "DECIMAL")
  private double price;
  @Column(name = "extra_place")
  private int extraPlace;
  @Column(name = "price_of_extra_place", columnDefinition = "DECIMAL")
  private double priceOfExtraPlace;
  private String currency;

  @OneToMany(mappedBy = "roomCategory")
  private Set<Room> rooms = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
          name = "room_categories_additional_services",
          joinColumns = { @JoinColumn(name = "category_id") },
          inverseJoinColumns = { @JoinColumn(name = "additional_service_id") }
  )
  Set<AdditionalService> additionalServices = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
          name = "room_categories_facilities",
          joinColumns = { @JoinColumn(name = "room_category_id") },
          inverseJoinColumns = { @JoinColumn(name = "facilities_id") }
  )
  Set<Facility> facilities = new HashSet<>();

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


  public RoomLevel getLevel() {
    return roomLevel;
  }

  public void setLevel(RoomLevel roomLevel) {
    this.roomLevel = roomLevel;
  }


  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public int getExtraPlace() {
    return extraPlace;
  }

  public void setExtraPlace(int extraPlace) {
    this.extraPlace = extraPlace;
  }


  public double getPriceOfExtraPlace() {
    return priceOfExtraPlace;
  }

  public void setPriceOfExtraPlace(double priceOfExtraPlace) {
    this.priceOfExtraPlace = priceOfExtraPlace;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public Set<AdditionalService> getAdditionalServices() {
    return additionalServices;
  }

  public void setAdditionalServices(Set<AdditionalService> additionalServices) {
    this.additionalServices = additionalServices;
  }


  public Set<Room> getRooms() {
    return rooms;
  }

  public void setRooms(Set<Room> rooms) {
    this.rooms = rooms;
  }

  public Set<Facility> getFacilities() {
    return facilities;
  }

  public void setFacilities(Set<Facility> facilities) {
    this.facilities = facilities;
  }

}
