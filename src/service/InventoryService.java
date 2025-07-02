package service;

import dao.FileDaoImpl;
import dao.IDao;
import model.Product;
import java.util.List;

public class InventoryService {
    private IDao dao;

    public InventoryService(IDao dao) {
        this.dao = dao;
    }
    public List<Product> getAllProducts() {
        return dao.loadProducts();
    }
    public void addProduct(Product p) {
        List<Product> products = dao.loadProducts();

        // בדיקה אם קיים מוצר עם אותו שם
        boolean exists = products.stream()
                .anyMatch(prod -> prod.getName().equalsIgnoreCase(p.getName()));

        if (!exists) {
            products.add(p);
            dao.saveProducts(products);
        } else {
            System.out.println("Product with name '" + p.getName() + "' already exists.");
        }
    }
    public void removeProduct(String name ) {
        List<Product> products = dao.loadProducts();
        products.removeIf(p -> p.getName().equalsIgnoreCase(name));
        dao.saveProducts(products);
    }
    public void UpdateProduct(Product updatedProduct) {
        List<Product> products = dao.loadProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(updatedProduct.getName())) {
                products.set(i, updatedProduct);
                break;
            }
        }
        dao.saveProducts(products);
    }
}
