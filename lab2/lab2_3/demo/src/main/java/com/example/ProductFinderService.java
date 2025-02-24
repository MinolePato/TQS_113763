package com.example;

import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Optional;
import org.json.JSONObject;

public class ProductFinderService {
    private String API_PRODUCTS;
    private SimpleHttpClient httpClient;

    public ProductFinderService(SimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.API_PRODUCTS = "https://fakestoreapi.com/products";
    }

    public Optional<Product> findProductDetail(int id) throws IOException {
        String response = httpClient.doHttpGet(API_PRODUCTS + "/" + id);
        if (response == null) {
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Product product = objectMapper.readValue(response, Product.class);
            return Optional.of(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
