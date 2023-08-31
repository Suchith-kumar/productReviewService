package com.signify.reviews.dao;

import com.signify.reviews.dao.model.ReviewEntity;
import com.signify.reviews.dao.repo.ReviewRepo;
import com.signify.reviews.dto.ReviewDto;
import com.signify.reviews.dto.request.ReviewFilterDto;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReviewDaoTest {

    @InjectMocks
    private ReviewDao reviewDao;

    @Mock
    private ReviewRepo reviewRepo;

    ArgumentCaptor<Specification<ReviewEntity>> specCaptor = ArgumentCaptor.forClass(Specification.class);


    @Test
    void shouldCreateReviewRatings() {

        ReviewDto requestReviewDto = ReviewDto.builder()
                .productName("Amazon Alexa")
                .review("Pero deberia de poder cambiarle el idioma a alexa")
                .rating(3)
                .title("Excelente")
                .reviewSource("iTunes")
                .author("WarcryxD")
                .reviewedDate(Timestamp.from(Instant.now()))
                .build();

        ReviewEntity savedReviewEntity = new ReviewEntity(1,
                "Amazon Alexa", "Pero deberia de poder cambiarle el idioma a alexa",
                "iTunes", 3, "Excelente", "Amazon Alexa", Timestamp.from(Instant.now()));

        when(reviewRepo.save(any(ReviewEntity.class))).thenReturn(savedReviewEntity);

        ReviewDto reviewDto = reviewDao.createReview(requestReviewDto);

        assertThat(reviewDto.getRating()).isSameAs(requestReviewDto.getRating());

    }

    @Test
    void shouldBulkCreateReviewRatings() {

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
        reviewDao.bulkCreateReview(bulkReviews);

        verify(reviewRepo, times(1)).saveAll(anyList());

    }

    @Test
    void shouldFetchAvgRatingByMonth() {

        List<Tuple> fetchedTuples = new ArrayList<>();

        NativeQueryTupleTransformer nativeQueryTupleTransformer = new NativeQueryTupleTransformer();

        fetchedTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(new Object[]{new BigDecimal(2),
                        Integer.parseInt("1"), new String("iTunes")},
                new String[]{"0", "1", "2"}));

        when(reviewRepo.findAvgRatingByMonthAndStore()).thenReturn(fetchedTuples);

        reviewDao.fetchAvgRatingByMonth();

        verify(reviewRepo, times(1)).findAvgRatingByMonthAndStore();
    }

    @Test
    void shouldFetchTotalRatingByStores() {

        List<Tuple> fetchedTuples = new ArrayList<>();

        NativeQueryTupleTransformer nativeQueryTupleTransformer = new NativeQueryTupleTransformer();

        fetchedTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(new Object[]{new String("iTunes"),
                        Integer.parseInt("1"),
                        new BigInteger("2")},
                new String[]{"0", "1", "2"}));

        when(reviewRepo.findTotalRatingbyStore()).thenReturn(fetchedTuples);

        reviewDao.fetchTotalRatingByStores();

        verify(reviewRepo, times(1)).findTotalRatingbyStore();

    }

    @Test
    void shouldSearchWithDynamicFilters() {


        ReviewFilterDto filterDto = ReviewFilterDto.builder()
                .store("iTunes")
                .rating(2)
                .startDate("2018-09-12")
                .endDate("2018-10-12")
                .build();

        reviewDao.searchWithDynamicFilters(filterDto);

        verify(reviewRepo, times(1)).findAll(specCaptor.capture());
    }
}
