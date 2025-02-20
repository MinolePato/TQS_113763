package com.example;

public class ProductFinderService implements  ISimpleHttpClient{
    public String API_PRODUCTS;
    public ISimpleHttpClient httpClient;
    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
}
