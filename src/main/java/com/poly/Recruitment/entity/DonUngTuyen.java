package com.poly.Recruitment.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DonUngTuyen {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationID;

    @ManyToOne
    @JoinColumn(name = "JobID") // Ensure this column matches the DB schema
    private TinTuyenDung job;

    @ManyToOne
    @JoinColumn(name = "JobseekerID") // Ensure this column matches the DB schema
    private NguoiTimViec jobseeker;
    @Column(name = "name", columnDefinition = "nvarchar(max)")
    private String name;
    private String phone;
    
    @Column(name = "address", columnDefinition = "nvarchar(max)")
    private String address;

    @Column(length = 255 ,columnDefinition = "nvarchar(max)")
    private String status;

    @Column(name = "cv_url", columnDefinition = "nvarchar(max)")
    private String cvURL; // Trường lưu URL hoặc đường dẫn của CV
}