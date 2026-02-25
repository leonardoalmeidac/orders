package org.unir.msbookpayment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.unir.msbookpayment.client.dto.BookCatalogueResponse;

@Component
@RequiredArgsConstructor
public class BookCatalogueClient {

    private final RestTemplate restTemplate;

    @Value("${catalogue.base-url}")
    private String catalogueBaseUrl;

    public BookCatalogueResponse getBookById(String bookId) {
        try {
            return restTemplate.getForObject(
                    catalogueBaseUrl + "/books/{id}",
                    BookCatalogueResponse.class,
                    bookId);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
