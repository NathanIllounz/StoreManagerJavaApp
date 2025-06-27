package algorithm.interfaces;

import model.Product;
import java.util.List;

public interface IProductRecommendation {
    List<Product> recommend(List<Product> products);
}
