package com.signify.reviews.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSearchResult {
    private String review;

    private String author;

    private String reviewSource;

    private Integer rating;

    private String title;

    private String productName;

    private Timestamp reviewedDate;
}
