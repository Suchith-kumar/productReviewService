package com.signify.reviews.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewRequestDto {

    private String review;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Review Source is required")
    @JsonProperty("review_source")
    private String reviewSource;

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank(message = "Review title is required")
    private String title;

    @NotBlank(message = "Product Name is required")
    @JsonProperty("product_name")
    private String productName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("reviewed_date")
    private Date reviewedDate;
}
