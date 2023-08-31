package com.signify.reviews.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reviewServiceOpenAPI() {

        Contact contact = new Contact();
        contact.setEmail("kvsuchit@gmail.com");
        contact.setName("Suchith");
        contact.setUrl("https://www.bezkoder.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Customer Review ratings API Spec")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage Ratings of the product.")
                .license(mitLicense);

        return new OpenAPI().info(info);

    }
}
