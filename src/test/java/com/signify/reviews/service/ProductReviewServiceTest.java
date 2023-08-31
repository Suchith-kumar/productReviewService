package com.signify.reviews.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.signify.reviews.dao.ReviewDao;
import com.signify.reviews.dao.model.ReviewEntity;
import com.signify.reviews.dto.AverageRatingDto;
import com.signify.reviews.dto.RatingDto;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.request.ReviewFilterDto;
import com.signify.reviews.dto.response.AverageRate;
import com.signify.reviews.dto.response.ReviewSearchResult;
import com.signify.reviews.dto.response.TotalRating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductReviewServiceTest {

    @InjectMocks
    private ProductReviewService productReviewService;

    @Mock
    private ReviewDao reviewDao;

    @Test
    void shouldCreateProductReview() {

        ReviewDto requestReviewDto = ReviewDto.builder()
                .productName("Amazon Alexa")
                .review("Pero deberia de poder cambiarle el idioma a alexa")
                .rating(3)
                .title("Excelente")
                .reviewSource("iTunes")
                .author("WarcryxD")
                .reviewedDate(Timestamp.from(Instant.now()))
                .build();
        when(reviewDao.createReview(any(ReviewDto.class))).thenReturn(ReviewDto.builder()
                .id(1)
                .productName("Amazon Alexa")
                .review("Pero deberia de poder cambiarle el idioma a alexa")
                .rating(3)
                .title("Excelente")
                .reviewSource("iTunes")
                .author("WarcryxD")
                .reviewedDate(Timestamp.from(Instant.now()))
                .build());

        ReviewDto reviewDto = productReviewService.createProductReview(requestReviewDto);

        assertThat(reviewDto.getRating()).isSameAs(requestReviewDto.getRating());

    }

    @Test
    void shouldBulkSaveReviewRatings() {

        ReviewDto requestReviewDto = ReviewDto.builder()
                .productName("Amazon Alexa")
                .review("Pero deberia de poder cambiarle el idioma a alexa")
                .rating(3)
                .title("Excelente")
                .reviewSource("iTunes")
                .author("WarcryxD")
                .reviewedDate(Timestamp.from(Instant.now()))
                .build();

        List<ReviewDto> bulkReviews = new ArrayList<>();
        bulkReviews.add(requestReviewDto);
        productReviewService.bulkCreateProductReview(bulkReviews);

        verify(reviewDao, times(1)).bulkCreateReview(anyList());

    }


    @Test
    void shouldFetchAvgRatingByMonth() throws IOException {

        File avgRatingJson = new File("src/test/resources/averageRating.json");
        ObjectMapper mapper = new ObjectMapper();
        List<AverageRatingDto> avgRating = mapper.readValue(avgRatingJson,
                new TypeReference<List<AverageRatingDto>>() {
                });

        when(reviewDao.fetchAvgRatingByMonth()).thenReturn(avgRating);

        Map<String, List<AverageRate>> mappedView = productReviewService.fetchAvgRatingByMonth();

        assertThat(mappedView.size()).isEqualTo(2);

    }

    @Test
    void shouldFetchTotalRating() throws IOException {

        File totalRatingJson = new File("src/test/resources/totalRating.json");
        ObjectMapper mapper = new ObjectMapper();
        List<RatingDto> totalRating = mapper.readValue(totalRatingJson,
                new TypeReference<List<RatingDto>>() {
                });

        when(reviewDao.fetchTotalRatingByStores()).thenReturn(totalRating);

        Map<String, List<TotalRating>> mappedView = productReviewService.fetchTotalRating();

        assertThat(mappedView.size()).isEqualTo(2);

    }

    @Test
    void shouldSearchReview() throws IOException {

        ReviewFilterDto filterDto = ReviewFilterDto.builder()
                .store("iTunes")
                .rating(2)
                .build();

        File reviewRecordJson = new File("src/test/resources/reviewRecord.json");
        ObjectMapper mapper = new ObjectMapper();
        List<ReviewEntity> reviewRecords = mapper.readValue(reviewRecordJson,
                new TypeReference<List<ReviewEntity>>() {
                });

        when(reviewDao.searchWithDynamicFilters(any(ReviewFilterDto.class))).thenReturn(reviewRecords);

        List<ReviewSearchResult> searchResults = productReviewService.searchReview(filterDto);

        assertThat(searchResults.size()).isEqualTo(3);

    }


}
