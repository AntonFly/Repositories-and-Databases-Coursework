package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "direction")
public class Direction {

  @Id
  private int id;
  private String country;
  private String city;
  @Column(name = "average_price", nullable = false, columnDefinition = "DECIMAL")
  private double averagePrice;
  @Column(name = "flight_price", nullable = false, columnDefinition = "DECIMAL")
  private double flightPrice;
  private String currency;
  @Column(name = "climate", columnDefinition = "MEDIUMTEXT")
  private String climate;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }


  public double getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(double averagePrice) {
    this.averagePrice = averagePrice;
  }


  public double getFlightPrice() {
    return flightPrice;
  }

  public void setFlightPrice(double flightPrice) {
    this.flightPrice = flightPrice;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public String getClimate() {
    return climate;
  }

  public void setClimate(String climate) {
    this.climate = climate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Direction)) return false;
    Direction direction = (Direction) o;
    return getId() == direction.getId()
            && Double.compare(direction.getAveragePrice(), getAveragePrice()) == 0
            && Double.compare(direction.getFlightPrice(), getFlightPrice()) == 0
            && getCountry().equals(direction.getCountry())
            && getCity().equals(direction.getCity())
            && getCurrency().equals(direction.getCurrency())
            && Objects.equals(getClimate(), direction.getClimate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountry(), getCity(), getAveragePrice(), getFlightPrice(), getCurrency(), getClimate());
  }

}
