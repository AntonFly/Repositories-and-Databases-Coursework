package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  private int id;
  private String name;
  private String surname;
  private String patronymic;
  @Column(name = "phone_number", nullable = false)
  private int phoneNumber;
  @Column(name = "bio", columnDefinition = "TEXT")
  private String bio;
  private java.sql.Date birthday;
  private String email;
  @Column(name = "password", columnDefinition = "TEXT")
  private String password;


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


  public int getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(int phoneNumber) {
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


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
