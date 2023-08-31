package com.signify.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Integer id;

    private String review;

    private String author;

    private String reviewSource;

    private Integer rating;

    private String title;

    private String productName;

    private Timestamp reviewedDate;
}
