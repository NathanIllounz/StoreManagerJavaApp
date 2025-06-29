package test;

import algorithm.impl.HighProfitRecommendation;
import algorithm.interfaces.IProductRecommendation;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class TestHighProfitRecommendation {
    public static void main(String[] args) {
        // יצירת רשימת מוצרים
        List<Product> products = new ArrayList<>();
        products.add(new Product("Milk", 10, 5, 30));     // רווח: 5
        products.add(new Product("Eggs", 8, 3, 5));       // רווח: 5
        products.add(new Product("Bread", 6, 1, 12));     // רווח: 5
        products.add(new Product("Cheese", 15, 9, 2));    // רווח: 6
        products.add(new Product("Butter", 12, 8, 9));    // רווח: 4
        products.add(new Product("Yogurt", 9, 4, 3));     // רווח: 5
        products.add(new Product("Ice Cream", 20, 5, 6)); // רווח: 15

        // הפעלת האלגוריתם
        IProductRecommendation algo = new HighProfitRecommendation();
        List<Product> recommended = algo.recommend(products);

        // הדפסת ההמלצות – רק שם ורווח
        System.out.println("Top 5 most profitable products:");
        for (Product p : recommended) {
            int profit = p.getSellingPrice() - p.getBuyingPrice();
            System.out.println(p.getName() + " (profit: " + profit + ")");
        }
    }
}

