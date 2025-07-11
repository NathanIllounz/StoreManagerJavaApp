package algorithm.impl;

import algorithm.interfaces.IProductRecommendation;
import model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class HighProfitRecommendation implements IProductRecommendation {

    @Override
    public List<Product> recommend(List<Product> products) {
        return products.stream()
                .sorted((p1, p2) -> Double.compare(
                        (p2.getSellingPrice() - p2.getBuyingPrice()),
                        (p1.getSellingPrice() - p1.getBuyingPrice())
                ))
                .limit(5)
                .collect(Collectors.toList());

        }
}