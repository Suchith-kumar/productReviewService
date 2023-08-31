package com.signify.reviews.dao.filter;

import com.signify.reviews.dao.model.ReviewEntity;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;


public class ReviewFilterSpecification {

    private ReviewFilterSpecification(){}

    public static Specification<ReviewEntity> hasRating(Integer rating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("rating"), rating);
    }

    public static Specification<ReviewEntity> hasStoreType(String store) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("reviewSource"), store);
    }

    public static Specification<ReviewEntity> hasDateRange(Timestamp beginDate, Timestamp endDate) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("reviewedDate"), beginDate, endDate);
    }

}
