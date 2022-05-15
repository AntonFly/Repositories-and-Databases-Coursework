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
  @Column(name = "location_id", nullable = false)
  private int locationId;
  @Column(name = "class_id", nullable = false)
  private int classId;
  @Column(name = "type_id", nullable = false)
  private int typeId;
  private String disposition;
  @Column(name = "description", columnDefinition = "MEDIUMTEXT")
  private String description;

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
          name = "hotels_excursions",
          joinColumns = { @JoinColumn(name = "hotel_id") },
          inverseJoinColumns = { @JoinColumn(name = "excursion_id") }
  )
  Set<Excursion> excursions = new HashSet<>();


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


  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }


  public int getClassId() {
    return classId;
  }

  public void setClassId(int classId) {
    this.classId = classId;
  }


  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
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

}
