package com.example;

public class Product {
    public int id;
    public String image;
    public String description;
    public Double price;
    public String title;
    public String category;
    public Product(int id, String image, String description, Double price, String title, String category) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
    }
    public Product() {
    }
    
}