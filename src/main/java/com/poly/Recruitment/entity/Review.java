package com.poly.Recruitment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewID;

	@ManyToOne
	@JoinColumn(name = "job_id")	
	private TinTuyenDung job;

	@ManyToOne
	@JoinColumn(name = "reviewer_Id")
	@JsonBackReference
	private NguoiTimViec reviewer;


	private Integer rating;

	@Column(columnDefinition = "nvarchar(max)")
	private String comment;


}