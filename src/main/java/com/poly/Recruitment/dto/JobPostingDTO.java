package com.poly.Recruitment.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class JobPostingDTO {
    private Long jobId;
    private String title;
    private String description;
    private String position;
    private String requirement;
    private String benefits;
    private LocalDate postedDate;
    private LocalDate endDate;
    private String postedBy;
    private MultipartFile image; // Để xử lý hình ảnh
    private Double salary;
    private String province;
    private String industry;

    // Getters and Setters
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public LocalDate getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDate postedDate) { this.postedDate = postedDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }
    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
}
