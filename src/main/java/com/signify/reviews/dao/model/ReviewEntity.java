package com.signify.reviews.dao.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "customer_review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    private String review;

    private String author;

    @Column(name="review_source")
    private String reviewSource;

    private Integer rating;

    private String title;

    @Column(name="product_name")
    private String productName;

    @Column(name="reviewed_date")
    private Timestamp reviewedDate;

}
