package model;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private int sellingPrice;
    private int buyingPrice;
    private int stock;

    public Product(String name, int sellingPrice, int buyingPrice, int stock) {
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
        this.stock = stock;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // ToString
    @Override
    public String toString() {
        return "Product{name='" + name + '\'' +
                ", sellingPrice=" + sellingPrice +
                ", buyingPrice=" + buyingPrice +
                ", stock=" + stock + '}';
    }
}
