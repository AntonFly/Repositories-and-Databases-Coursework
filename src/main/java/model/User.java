package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id
  private int id;
  private String name;
  private String surname;
  private String patronymic;
  @Column(name = "phone_number", nullable = false, columnDefinition = "INTEGER")
  private long phoneNumber;
  @Column(name = "bio", columnDefinition = "TEXT")
  private String bio;
  private java.sql.Date birthday;
  private String email;
  @Column(name = "chat_id", columnDefinition = "BIGINT")
  private long chat_id;

  @OneToMany(mappedBy = "user")
  private Set<Order> orders = new HashSet<>();


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


  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }


  public String getPatronymic() {
    return patronymic;
  }

  public void setPatronymic(String patronymic) {
    this.patronymic = patronymic;
  }


  public long getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(long phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }


  public java.sql.Date getBirthday() {
    return birthday;
  }

  public void setBirthday(java.sql.Date birthday) {
    this.birthday = birthday;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public long getChatId() {
    return chat_id;
  }

  public void setChatId(long chat_id) {
    this.chat_id = chat_id;
  }


  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }

}
