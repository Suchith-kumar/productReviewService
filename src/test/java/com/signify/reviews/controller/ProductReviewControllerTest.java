package com.signify.reviews.controller;

import com.signify.reviews.exception.RequestMappingException;
import com.signify.reviews.service.ProductReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductReviewController.class)
public class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductReviewService productReviewService;

    @Test
    public void shouldSaveReview() throws Exception {
        //Given
        String request = "{" +
                "\"review\": \"Pero deberia de poder cambiarle el idioma a alexa\"," +
                "\"author\": \"WarcryxD\"," +
                "\"review_source\": \"iTunes\"," +
                "\"rating\": 4," +
                "\"title\": \"Excelente\"," +
                "\"product_name\": \"Amazon Alexa\"," +
                "\"reviewed_date\": \"2018-01-12T02:27:03.000Z\"" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/review")
                .accept(MediaType.APPLICATION_JSON).content(request)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(productReviewService, times(1)).createProductReview(any());

    }

    @Test()
    public void shouldReturnBadRequestForSaveReview() throws Exception {
        String request = "{" +
                "\"review\": \"Pero deberia de poder cambiarle el idioma a alexa\"," +
                "\"author\": \"WarcryxD\"," +
                "\"review_source\": \"iTunes\"," +
                "\"rating\": 6," +
                "\"title\": \"Excelente\"," +
                "\"product_name\": \"Amazon Alexa\"," +
                "\"reviewed_date\": \"2018-01-12T02:27:03.000Z\"" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/review")
                .accept(MediaType.APPLICATION_JSON).content(request)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test()
    public void shouldReturnSearchResult() throws Exception {

        String requestParams = "rating=4&startDate=2017-9-10&endDate=2017-10-12&store=GooglePlayStore";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/search?"+requestParams)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(productReviewService, times(1)).searchReview(any());

    }

    @Test()
    public void shouldReturnBadRequestForSaveResult() throws Exception {

        String requestParams = "rating=4&endDate=2017-10-12&store=GooglePlayStore";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/search?"+requestParams)
                .contentType(MediaType.APPLICATION_JSON);
       mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof RequestMappingException))
               .andExpect(result -> assertEquals("Both start & end Date required",
                       result.getResolvedException().getMessage()));

    }

    @Test()
    public void shouldReturnAverageMonthlyRating() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/avgMonthlyRating")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(productReviewService, times(1)).fetchAvgRatingByMonth();

    }

    @Test()
    public void shouldReturnAllReviewRatings() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/totalRatings")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(productReviewService, times(1)).fetchTotalRating();

    }

}
