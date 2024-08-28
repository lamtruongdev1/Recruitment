package com.poly.Recruitment.dto;

public class ReviewRequest {
    private Long reviewerId;
    private Long jobseekerID;
    private Long jobId;
    private Integer rating;
    private String comment;

    // Constructor with parameters
    public ReviewRequest(Long reviewerId, Long jobseekerID, Long jobId, Integer rating, String comment) {
        this.reviewerId = reviewerId;
        this.jobseekerID = jobseekerID;
        this.jobId = jobId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and setters
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Long getJobseekerID() { return jobseekerID; }
    public void setJobseekerID(Long jobseekerID) { this.jobseekerID = jobseekerID; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
