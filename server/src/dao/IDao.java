package dao;

import model.Product;
import java.util.List;

public interface IDao {
    List<Product> loadProducts();
    void saveProducts(List<Product> products);
}
