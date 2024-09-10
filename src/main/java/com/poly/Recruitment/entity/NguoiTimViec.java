package com.poly.Recruitment.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.Recruitment.entity.NhaTuyenDung.NhaTuyenDungBuilder;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nguoi_tim_viec")
public class NguoiTimViec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobseekerID;

    @ManyToOne
    @JoinColumn(name = "user_id") // Ensure the column name matches your DB schema
    private User user;

    @Column(name = "CV_id", columnDefinition = "nvarchar(max)")
    private String CVID;

    @Column(name = "payment_id", columnDefinition = "nvarchar(max)")
    private String paymentID;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "gender", columnDefinition = "nvarchar(max)")
    private String gender;

    @Column(name = "skill", columnDefinition = "nvarchar(max)")
    private String skill;

    @Column(name = "CV_URL", columnDefinition = "nvarchar(max)")
    private String CVURL;

    @OneToMany(mappedBy = "reviewer")
    @JsonManagedReference
    private List<Review> reviews;



    @Override
    public String toString() {
        return "NguoiTimViec [jobseekerID=" + jobseekerID +
               ", user=" + (user != null ? user.getName() : "No User") +
               ", birthday=" + birthday +
               ", gender=" + gender +
               ", skill=" + skill +
               ", reviewsCount=" + (reviews != null ? reviews.size() : 0) + "]";
    }



	
}