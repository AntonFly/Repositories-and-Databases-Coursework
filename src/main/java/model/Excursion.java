package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "excursions")
public class Excursion {

  @Id
  private int id;
  private String name;
  private String location;
  @Column(name = "price_per_person", nullable = false, columnDefinition = "DECIMAL")
  private double pricePerPerson;
  private String currency;
  @Column(name = "min_age", nullable = false)
  private int minAge;
  @Column(name = "min_members", nullable = false)
  private int minMembers;
  @Column(name = "max_members", nullable = false)
  private int maxMembers;
  @Column(name = "guide", columnDefinition = "TINYINT")
  private int guide;
  private String language;

  @ManyToMany(mappedBy = "excursions")
  private Set<Hotel> hotels = new HashSet<>();

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


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }


  public double getPricePerPerson() {
    return pricePerPerson;
  }

  public void setPricePerPerson(double pricePerPerson) {
    this.pricePerPerson = pricePerPerson;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public int getMinAge() {
    return minAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }


  public int getMinMembers() {
    return minMembers;
  }

  public void setMinMembers(int minMembers) {
    this.minMembers = minMembers;
  }


  public int getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(int maxMembers) {
    this.maxMembers = maxMembers;
  }


  public int getGuide() {
    return guide;
  }

  public void setGuide(int guide) {
    this.guide = guide;
  }


  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }


  public Set<Hotel> getHotels() {
    return hotels;
  }

  public void setHotels(Set<Hotel> hotels) {
    this.hotels = hotels;
  }


}
