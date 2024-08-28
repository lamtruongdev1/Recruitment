package com.poly.Recruitment.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonUngTuyenDTO {
    private Long applicationID;
    private Long jobId;
    private Long jobseekerID;
    private String status;
    @Column(name = "cv_url", columnDefinition = "nvarchar(max)")
    private String cvURL; // Trường lưu URL hoặc đường dẫn của CV
}
