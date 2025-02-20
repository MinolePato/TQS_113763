package com.example;

public class Stock {
    public String label;
    public int quantity;
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Stock(String label, int quantity) {
        this.label = label;
        this.quantity = quantity;
    }
    
}
