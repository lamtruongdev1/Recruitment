package com.poly.Recruitment.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCV;

    @ManyToOne
    @JoinColumn(name = "JobseekerID")
    private NguoiTimViec jobseeker;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private BigDecimal price;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String fileURL;

    @ManyToMany(mappedBy = "cvs", cascade = CascadeType.ALL)
    @JsonBackReference 
    private List<Cart> carts;

    @PreRemove
    private void removeCarts() {
        if (carts != null) {
            for (Cart cart : carts) {
                cart.getCvs().remove(this);
            }
        }
    }
}
