package com.signify.reviews;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReviewsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ReviewsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenRequestShouldAddReviewSuccessfully() throws Exception {
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

        ResultActions response = mockMvc.perform(requestBuilder);
        response.andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    void givenFilterShouldReturnSearchResult() throws Exception {
        //Given
        String requestParams = "rating=4&startDate=2017-9-10&endDate=2017-10-12&store=GooglePlayStore";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/search?" + requestParams)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(requestBuilder);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(40)));

    }

    @Test()
    public void shouldReturnAverageMonthlyRatingSuccessfully() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/avgMonthlyRating")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(requestBuilder);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));

    }

    @Test()
    public void shouldReturnAllReviewRatingsSuccessfully() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/review/totalRatings")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(requestBuilder);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));

    }


}
