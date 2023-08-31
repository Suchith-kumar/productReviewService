package com.signify.reviews.dao.repo;

import com.signify.reviews.dao.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Integer>, JpaSpecificationExecutor<ReviewEntity> {

    @Query(value = "SELECT round(sum(" +
            "case when rating = 1 then rating*1.0 when rating = 2 then rating * 2.0 " +
            "when rating = 3 then rating * 3.0 when rating = 4 then rating * 4.0 when rating = 5 then rating * 5.0 end" +
            ") / sum(rating),1) as avg_rating, month(reviewed_date) as rating_month , review_source " +
            "FROM CUSTOMER_REVIEW group by rating_month,review_source",  nativeQuery = true)
    List<Tuple> findAvgRatingByMonthAndStore();

    @Query(value = "SELECT review_source ,rating,Count(*) as total from customer_review group by rating,review_source",
            nativeQuery = true)
    List<Tuple> findTotalRatingbyStore();
}
