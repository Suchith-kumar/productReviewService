package com.signify.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AverageRatingDto {

    private Float averageRating;
    private Integer ratingMonth;
    private String reviewSource;

}
