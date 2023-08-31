package com.signify.reviews;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.mapper.ReviewMapper;
import com.signify.reviews.dto.request.ReviewRequestDto;
import com.signify.reviews.service.ProductReviewService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class ReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(ProductReviewService productReviewService) {
		return args -> {
			// read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<ReviewRequestDto>> typeReference = new TypeReference<List<ReviewRequestDto>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json_data/alexa.json");
			try {
				List<ReviewRequestDto> prePopulatedReviews = mapper.readValue(inputStream,typeReference);
				List<ReviewDto> reviewsDto = ReviewMapper.bulkMapToCustomerReviewDto(prePopulatedReviews);;
				productReviewService.bulkCreateProductReview(reviewsDto);
				System.out.println("Reviews Saved!");
			} catch (IOException e){
				System.out.println("Unable to save Reviews: " + e.getMessage());
			}
		};
	}
}
