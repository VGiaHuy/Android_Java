package com.dinhthikimthoa.ontapthi.Models;

public class Product {
    int IdSP;
    String Name;
    float Price;

    public Product(int idSP, String name, float price) {
        IdSP = idSP;
        Name = name;
        Price = price;
    }

    public int getIdSP() {
        return IdSP;
    }

    public void setIdSP(int idSP) {
        IdSP = idSP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }
}
