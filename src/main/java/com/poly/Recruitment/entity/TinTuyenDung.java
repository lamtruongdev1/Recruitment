package com.poly.Recruitment.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tin_tuyen_dung")
public class TinTuyenDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;
    
    @Column(name = "title", columnDefinition = "nvarchar(max)")
    private String title;
    
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;
    
    @Column(name = "position", columnDefinition = "nvarchar(max)")
    private String position;
    
    @Column(name = "requirement", columnDefinition = "nvarchar(max)")
    private String requirement;
    
    @Column(name = "benefits", columnDefinition = "nvarchar(max)")
    private String benefits;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "posted_by", columnDefinition = "nvarchar(max)")
    private String postedBy;

    @Column(name = "image", columnDefinition = "nvarchar(max)")
    private String image;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "province", columnDefinition = "nvarchar(max)")
    private String province;


    @Column(name = "industry", columnDefinition = "nvarchar(max)")
    private String industry;  

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<Review> reviews;
}
