package com.signify.reviews.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AverageRate {

    private Float averageRating;
    private Integer ratingMonth;

}
