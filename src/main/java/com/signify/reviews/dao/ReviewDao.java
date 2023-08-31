package com.signify.reviews.dao;

import com.signify.reviews.dao.filter.ReviewFilterSpecification;
import com.signify.reviews.dao.model.ReviewEntity;
import com.signify.reviews.dao.repo.ReviewRepo;
import com.signify.reviews.dto.AverageRatingDto;
import com.signify.reviews.dto.RatingDto;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.mapper.ReviewMapper;
import com.signify.reviews.dto.request.ReviewFilterDto;
import com.signify.reviews.utils.ReviewDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReviewDao {
    private ReviewRepo reviewRepo;

    @Autowired
    public ReviewDao(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public ReviewDto createReview(ReviewDto reviewDto) {
        ReviewEntity customerReview = ReviewMapper.mapToCustomerReviewEntity(reviewDto);
        ReviewEntity savedReview = reviewRepo.save(customerReview);
        return ReviewMapper.mapToCustomerReviewDto(savedReview);
    }

    public void bulkCreateReview(List<ReviewDto> reviewDto) {
        List<ReviewEntity> reviews = ReviewMapper.bulkMapToCustomerReviewEntity(reviewDto);
        reviewRepo.saveAll(reviews);
    }

    public List<AverageRatingDto> fetchAvgRatingByMonth() {

        List<Tuple> avrgRatingResultSet = reviewRepo.findAvgRatingByMonthAndStore();

        List<AverageRatingDto> averageRatingDtos = new ArrayList<>();

        for (Tuple avgRating : avrgRatingResultSet) {
            AverageRatingDto ratingDto = new AverageRatingDto();

            BigDecimal ratingValue = avgRating.get(0, BigDecimal.class);
            ratingDto.setAverageRating(ratingValue.floatValue());

            ratingDto.setRatingMonth(avgRating.get(1, Integer.class));
            ratingDto.setReviewSource(avgRating.get(2, String.class));

            averageRatingDtos.add(ratingDto);
        }


        return averageRatingDtos;
    }

    public List<RatingDto> fetchTotalRatingByStores() {

        List<Tuple> totalReviewRatings = reviewRepo.findTotalRatingbyStore();

        List<RatingDto> ratingDtos = new ArrayList<>();

        for (Tuple rating : totalReviewRatings) {
            RatingDto ratingDto = new RatingDto();

            ratingDto.setReviewSource(rating.get(0, String.class));
            ratingDto.setRating(rating.get(1, Integer.class));

            BigInteger total = rating.get(2, BigInteger.class);
            ratingDto.setTotal(total.intValue());
            ratingDtos.add(ratingDto);
        }

        return ratingDtos;
    }

    public List<ReviewEntity> searchWithDynamicFilters(ReviewFilterDto filters) {

        Integer rating = filters.getRating();
        String store = filters.getStore();
        String startDate = filters.getStartDate();
        String endDate = filters.getEndDate();


        Specification<ReviewEntity> criteriaSpecification = Specification.where(null);

        if (rating != null) {
            criteriaSpecification = criteriaSpecification.and(ReviewFilterSpecification.hasRating(rating));
        }
        if (startDate != null && endDate != null) {
            criteriaSpecification = buildDateRangecriteria(startDate, endDate, criteriaSpecification);
        }
        if (store != null) {
            criteriaSpecification = criteriaSpecification.and(ReviewFilterSpecification.hasStoreType(store));
        }

        List<ReviewEntity> filteredReviews = reviewRepo.findAll(criteriaSpecification);

        return filteredReviews;

    }

    private Specification<ReviewEntity> buildDateRangecriteria(String startDate, String endDate,
                                                               Specification<ReviewEntity> criteria) {
        Date beginDate, endingDate;
        Timestamp beginTs, endTs;

        // parse from string to date
        beginDate = ReviewDateUtil.parseDate(startDate);
        endingDate = ReviewDateUtil.parseDate(endDate);

        // set the time hh:mm:ss values
        beginDate = ReviewDateUtil.setStartDateWithTime(beginDate);
        endingDate = ReviewDateUtil.setEndDateWithTime(endingDate);

        //convert to Timestamp
        beginTs = ReviewDateUtil.mapToTimestamp(beginDate);
        endTs = ReviewDateUtil.mapToTimestamp(endingDate);

        return criteria.and(ReviewFilterSpecification.hasDateRange(beginTs, endTs));

    }


}
