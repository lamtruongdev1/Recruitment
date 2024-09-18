package com.poly.Recruitment.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`User`")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String name;

    private String email;
    private String phone;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String address;
    @Lob
    @Column(name = "photo")
    private String photo;
    private String role;
    private String password;
    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private Cart cart;
    public User(Long userID) {
        this.userID = userID;
    }

}