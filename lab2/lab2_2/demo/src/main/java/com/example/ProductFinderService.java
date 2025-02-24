package com.example;

import org.json.JSONObject;

public class ProductFinderService implements ISimpleHttpClient {
    private final String API_PRODUCTS = "https://api.example.com/products";
    private final ISimpleHttpClient httpClient;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String doHttpGet(String url) {
        return httpClient.doHttpGet(url); 
    }

    public Product findProductDetails(int id) {
        String url = API_PRODUCTS + "/" + id;
        String response = doHttpGet(url);

        if (response == null || response.isEmpty()) {
            return null;
        }

        try {
            JSONObject json = new JSONObject(response);

            int productId = json.getInt("id");
            String image = json.getString("image");
            String description = json.getString("description");
            double price = json.getDouble("price");
            String title = json.getString("title");
            String category = json.getString("category");

            return new Product(productId, image, description, price, title, category);

        } catch (Exception e) {
            System.err.println("Erro ao processar JSON: " + e.getMessage());
            return null;
        }
    }
}
