package com.poly.Recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nha_tuyen_dung")
@Builder
public class NhaTuyenDung {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerID;

    @ManyToOne
    @JoinColumn(name = "user_id") // Ensure the column name matches your DB schema
    private User user;

    @Column(length = 255)
    private String companyName;

    @Column(length = 255)
    private String companyAddress;

    @Column(length = 20)
    private String companyPhone;

    @Column(length = 255)
    private String companyEmail;

    @Column(length = 255)
    private String companyWebsite;
    
    @Column(length = 255)
    private String logoFileName; // Add this line

    @Transient
    private String password;

}
