package com.example.ASTDemo;

public class TestInput2 {
    private double price;
    private int quantity;
    
    public TestInput2(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double calculateTotal() {
        return price * quantity;
    }
    
    public double calculateTotalWithDiscount(double discount) {
        double total = price * quantity;
        return total * (1 - discount);
    }
    
    public void printDetails() {
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Total: " + calculateTotal());
    }
}

