package com.example.ASTDemo;

public class TestInput1 {
    private String name;
    private int age;
    private boolean active;
    
    public TestInput1() {
        this.name = "";
        this.age = 0;
        this.active = false;
    }
    
    public TestInput1(String name, int age) {
        this.name = name;
        this.age = age;
        this.active = true;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Active: " + active);
    }
    
    public String toString() {
        return "TestInput1[name=" + name + ", age=" + age + ", active=" + active + "]";
    }
    
    public void processData(String data, int count, boolean flag, double value) {
        // Méthode avec plusieurs paramètres
        if (flag) {
            for (int i = 0; i < count; i++) {
                System.out.println("Processing: " + data + " - Value: " + value);
            }
        }
    }
    
    public int calculate(int a, int b, int c, int d, int e) {
        // Méthode avec 5 paramètres
        return a + b + c + d + e;
    }
}

