package algorithm.impl;

import algorithm.interfaces.IProductRecommendation;
import model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LowStockRecommendation implements  IProductRecommendation {

    @Override
    public List<Product> recommend(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparingInt(Product::getStock)) // מיון לפי כמות במלאי מהנמוך לגבוה
                .limit(3) // נחזיר עד 3 מוצרים בלבד
                .collect(Collectors.toList()); // נאסוף לרשימה חדשה
    }
}