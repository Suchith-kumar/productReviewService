package com.signify.reviews.controller;

import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.mapper.ReviewMapper;
import com.signify.reviews.dto.request.ReviewFilterDto;
import com.signify.reviews.dto.request.ReviewRequestDto;
import com.signify.reviews.dto.response.AverageRate;
import com.signify.reviews.dto.response.ReviewSearchResult;
import com.signify.reviews.dto.response.TotalRating;
import com.signify.reviews.service.ProductReviewService;
import io.swagger.v3.oas.annotations.OpenAPI30;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.signify.reviews.validator.ReviewValidator.validateFilters;

@RestController
@RequestMapping("/api/v1")
@Tag(name="Product-Review")
public class ProductReviewController {


    @Autowired
    private ProductReviewService productReviewService;

    @Operation(summary = "Adds a new Customer Reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the Review"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PostMapping("/review")
    public ResponseEntity<String> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto){

        ReviewDto requestDto = ReviewMapper.mapToCustomerReviewDto(reviewRequestDto);

        productReviewService.createProductReview(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully!");

    }

    @Operation(summary = "Search Customer Reviews with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered Review"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @GetMapping("/review/search")
    public ResponseEntity< List<ReviewSearchResult>> search(ReviewFilterDto reviewFilters){

        validateFilters(reviewFilters);
        List<ReviewSearchResult> results = productReviewService.searchReview(reviewFilters);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }


    @Operation(summary = "Fetch average Monthly Rating By Play Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Details of average Monthly Review Rating"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @GetMapping("/review/avgMonthlyRating")
    public ResponseEntity<Map<String,List<AverageRate>>> averageMonthlyRating(){

        Map<String,List<AverageRate>> result =  productReviewService.fetchAvgRatingByMonth();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch Overall rating by category per Play Store type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Details of total Rating by category"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @GetMapping("/review/totalRatings")
    public ResponseEntity<Map<String,List<TotalRating>>> allReviewRatings(){

        Map<String,List<TotalRating>>  result =  productReviewService.fetchTotalRating();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
