package test;

import algorithm.impl.LowStockRecommendation;
import algorithm.interfaces.IProductRecommendation;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class TestLowStockRecommendation {
    public static void main(String[] args) {

        // שלב 1: יצירת רשימת מוצרים
        List<Product> products = new ArrayList<>();
        products.add(new Product("Milk", 10, 5, 30));
        products.add(new Product("Eggs", 8, 4, 5));
        products.add(new Product("Bread", 6, 3, 12));
        products.add(new Product("Cheese", 15, 10, 2));
        products.add(new Product("Butter", 12, 7, 9));
        products.add(new Product("Yogurt", 9, 5, 3));

        // שלב 2: הפעלת האלגוריתם
        IProductRecommendation algo = new LowStockRecommendation();
        List<Product> recommended = algo.recommend(products);

        // שלב 3: הצגת ההמלצות
        System.out.println("Initial recommendations:");
        for (Product p : recommended) {
            System.out.println(p.getName() + " (stock: " + p.getStock() + ")");
        }

        // שלב 4: שינוי ערכים של מוצר (נניח העלנו את המלאי של Cheese)
        for (Product p : products) {
            if (p.getName().equals("Cheese")) {
                p.setStock(50);
            }
        }

        // שלב 5: הרצה מחדש של האלגוריתם!
        List<Product> recommendedAfterChange = algo.recommend(products);

        System.out.println("\nRecommendations after updating stock:");
        for (Product p : recommendedAfterChange) {
            System.out.println(p.getName() + " (stock: " + p.getStock() + ")");
        }
    }
}
