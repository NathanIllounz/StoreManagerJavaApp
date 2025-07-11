package test;

import model.Product;

public class TestProduct {
    public static void main(String[] args) {
        Product p = new Product("Milk", 10, 5, 20);

        System.out.println("Product name: " + p.getName());
        System.out.println("Selling price: " + p.getSellingPrice());
        System.out.println("Buying price: " + p.getBuyingPrice());
        System.out.println("Stock: " + p.getStock());

        System.out.println("Full toString: " + p);

        // שינוי ערכים
        p.setName("Chocolate Milk");
        p.setSellingPrice(12);
        p.setBuyingPrice(6);
        p.setStock(15);

        // הצגה לאחר שינוי
        System.out.println("Updated product: " + p);
    }

}
