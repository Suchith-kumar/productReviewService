package com.signify.reviews.validator;

import com.signify.reviews.dto.request.ReviewFilterDto;
import com.signify.reviews.exception.RequestMappingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewValidator {

    private ReviewValidator(){}

    private static final Object IS_NULL=null;


    public static Boolean validateFilters(ReviewFilterDto filterDto) {

        String startDate = filterDto.getStartDate();
        String endDate = filterDto.getEndDate();

        if ((startDate != IS_NULL && endDate == IS_NULL) ||
                (startDate == IS_NULL && endDate != IS_NULL)) {
            throw new RequestMappingException("Both start & end Date required");
        }

        Date beginDate = isValidDate(startDate);
        Date endingDate = isValidDate(endDate);

        if(beginDate.after(endingDate)){
            throw new RequestMappingException("start Date should be less than end Date");
        }

        return Boolean.TRUE;

    }

    private static Date isValidDate(String dateString) {

        Date date;
        String dateFormat = "yyyy-MM-dd";
        DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        dateFormatter.setLenient(false);

        try {
            date = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RequestMappingException("Invalid Date value provided");
        }

        return date;
    }
}
