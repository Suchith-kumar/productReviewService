package com.signify.reviews.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
public class ErrorDto {

    private Date timestamp;
    private List<String> message;
    private String details;

    public ErrorDto(Date timestamp, List<String> message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
