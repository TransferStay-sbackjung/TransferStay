package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "users")
public class User {
  @Id
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "name")
  private String name;


}