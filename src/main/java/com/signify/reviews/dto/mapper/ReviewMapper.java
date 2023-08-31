package com.signify.reviews.dto.mapper;

import com.signify.reviews.dao.model.ReviewEntity;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.request.ReviewRequestDto;
import com.signify.reviews.dto.response.ReviewSearchResult;
import com.signify.reviews.exception.RequestMappingException;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    // convert ReviewEntity into Review dto
    public static ReviewDto mapToCustomerReviewDto(ReviewEntity reviewEntity){

        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .review(reviewEntity.getReview())
                .author(reviewEntity.getAuthor())
                .reviewSource(reviewEntity.getReviewSource())
                .rating(reviewEntity.getRating())
                .title(reviewEntity.getTitle())
                .productName(reviewEntity.getProductName())
                .reviewedDate(reviewEntity.getReviewedDate())
                .build();
    }

    // convert Review dto into ReviewEntity jpa
    public static ReviewEntity mapToCustomerReviewEntity(ReviewDto reviewDto){
        return new ReviewEntity(
                reviewDto.getId(),
                reviewDto.getReview(),
                reviewDto.getAuthor(),
                reviewDto.getReviewSource(),
                reviewDto.getRating(),
                reviewDto.getTitle(),
                reviewDto.getProductName(),
                reviewDto.getReviewedDate()
        );
    }

    // convert bulk Review dto into list ReviewEntity jpa
    public static List<ReviewEntity> bulkMapToCustomerReviewEntity(List<ReviewDto> reviewsDto){


        List<ReviewEntity> reviewEntities = reviewsDto.stream()
                .map(dto -> mapToCustomerReviewEntity(dto))
                .collect(Collectors.toList());

        return reviewEntities;
    }

    // convert bulk Review dto into list ReviewEntity jpa
    public static List<ReviewDto> bulkMapToCustomerReviewDto(List<ReviewRequestDto> reviewsRequestDto){


        List<ReviewDto> reviewDtoList = reviewsRequestDto.stream()
                .map(dto -> mapToCustomerReviewDto(dto))
                .collect(Collectors.toList());

        return reviewDtoList;
    }

    // convert Review request dto into Review Dto
    public static ReviewDto mapToCustomerReviewDto(ReviewRequestDto reviewRequestDto) {

        try{

            Timestamp reviewedTs=new Timestamp(reviewRequestDto.getReviewedDate().getTime());

            return ReviewDto.builder()
                    .review(reviewRequestDto.getReview())
                    .author(reviewRequestDto.getAuthor())
                    .reviewSource(reviewRequestDto.getReviewSource())
                    .rating(reviewRequestDto.getRating())
                    .title(reviewRequestDto.getTitle())
                    .productName(reviewRequestDto.getProductName())
                    .reviewedDate(reviewedTs)
                    .build();

        }catch (RuntimeException ex){

            throw new RequestMappingException("Invalid request attributes provided");
        }

    }

    public static List<ReviewSearchResult> mapToReviewSearchResultDto(List<ReviewEntity> reviewEntities) {

        List<ReviewSearchResult> reviewSearchResults = reviewEntities.stream()
                .map(entity -> getReviewSearchResult(entity))
                .collect(Collectors.toList());

        return reviewSearchResults;

    }

    private static ReviewSearchResult getReviewSearchResult(ReviewEntity reviewEntity){
        return  ReviewSearchResult.builder()
                .review(reviewEntity.getReview())
                .title(reviewEntity.getTitle())
                .author(reviewEntity.getAuthor())
                .rating(reviewEntity.getRating())
                .reviewedDate(reviewEntity.getReviewedDate())
                .reviewSource(reviewEntity.getReviewSource())
                .productName(reviewEntity.getProductName())
                .build();
    }


}
