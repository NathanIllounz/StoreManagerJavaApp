package service;

import algorithm.impl.HighProfitRecommendation;
import algorithm.impl.LowStockRecommendation;
import algorithm.interfaces.IProductRecommendation;
import dao.IDao;
import model.Product;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class InventoryService {
    private IDao dao;

    public InventoryService(IDao dao) {
        this.dao = dao;
    }
    public List<Product> getAllProducts() {
        return dao.loadProducts();
    }
    public boolean addProduct(Product p) {
        List<Product> products = dao.loadProducts();
        boolean exists = products.stream().anyMatch(prod -> prod.getName().equalsIgnoreCase(p.getName()));
        if (!exists) {
            products.add(p);
            dao.saveProducts(products);
            return true;
        } else {
            return false;
        }
    }
    public void deleteProduct(String name ) {
        List<Product> products = dao.loadProducts();
        products.removeIf(p -> p.getName().equalsIgnoreCase(name));
        dao.saveProducts(products);
    }
    public boolean updateProduct(Product updatedProduct) {
        List<Product> products = dao.loadProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(updatedProduct.getName())) {
                products.set(i, updatedProduct);
                dao.saveProducts(products);
                return true;
            }
        }
        return false;
    }
    public List<Product> recommendLowStock(int limit) {
        IProductRecommendation algo = new LowStockRecommendation();
        return algo.recommend(dao.loadProducts()).stream().limit(limit).collect(Collectors.toList());
    }

    public List<Product> recommendHighProfit(int limit) {
        IProductRecommendation algo = new HighProfitRecommendation();
        return algo.recommend(dao.loadProducts()).stream().limit(limit).collect(Collectors.toList());
    }


    public Product findProductByName(String name) {
        List<Product> products = dao.loadProducts();
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

}
