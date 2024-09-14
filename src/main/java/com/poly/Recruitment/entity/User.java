package com.poly.Recruitment.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(length = 255) // Use length for VARCHAR types
    private String address;
    @Lob
    @Column(name = "photo")
    private String photo;
    private String role;
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<CV> cvs;

    public User(Long userID) {
        this.userID = userID;
    }

}