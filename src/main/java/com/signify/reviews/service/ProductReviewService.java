package com.signify.reviews.service;

import com.signify.reviews.dao.ReviewDao;
import com.signify.reviews.dao.model.ReviewEntity;
import com.signify.reviews.dto.AverageRatingDto;
import com.signify.reviews.dto.RatingDto;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.mapper.ReviewMapper;
import com.signify.reviews.dto.request.ReviewFilterDto;
import com.signify.reviews.dto.response.AverageRate;
import com.signify.reviews.dto.response.ReviewSearchResult;
import com.signify.reviews.dto.response.TotalRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ProductReviewService {

    private ReviewDao reviewDao;

    @Autowired
    public ProductReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public ReviewDto createProductReview(ReviewDto reviewDto) {
        return reviewDao.createReview(reviewDto);
    }

    public void bulkCreateProductReview(List<ReviewDto> reviewDtos) {
        reviewDao.bulkCreateReview(reviewDtos);
    }

    public List<ReviewSearchResult> searchReview(ReviewFilterDto reviewFilters) {

        List<ReviewEntity> searchReviewEntities = reviewDao.searchWithDynamicFilters(reviewFilters);

        List<ReviewSearchResult> searchReviews = ReviewMapper.mapToReviewSearchResultDto(searchReviewEntities);

        return searchReviews;
    }

    public Map<String, List<AverageRate>> fetchAvgRatingByMonth() {

        List<AverageRatingDto> searchResult = reviewDao.fetchAvgRatingByMonth();

        Map<String, List<AverageRate>> mappedView = searchResult.stream()
                .collect(groupingBy(AverageRatingDto::getReviewSource,
                        Collectors.mapping(e ->
                                        AverageRate.builder()
                                                .averageRating(e.getAverageRating())
                                                .ratingMonth(e.getRatingMonth())
                                                .build(),
                                Collectors.toList())));

        return mappedView;
    }

    public Map<String, List<TotalRating>> fetchTotalRating() {

        List<RatingDto> searchResult = reviewDao.fetchTotalRatingByStores();

        Map<String, List<TotalRating>> mappedView = searchResult.stream()
                .collect(groupingBy(RatingDto::getReviewSource,
                        Collectors.mapping(e ->
                                new TotalRating(e.getRating(), e.getTotal()), Collectors.toList())));

        return mappedView;
    }

}
