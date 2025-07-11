package test;

import dao.FileDaoImpl;
import dao.IDao;
import model.Product;

import java.util.List;

public class TestDao {
    public static void main(String[] args) {
        IDao dao = new FileDaoImpl();

        // שלב 1: טוען מוצרים מהקובץ
        List<Product> products = dao.loadProducts();
        System.out.println("Before:");
        for (Product p : products) {
            System.out.println(p.getName() + " (stock: " + p.getStock() + ")");
        }

        // שלב 2: מוסיף מוצר חדש לרשימה
        Product newProduct = new Product("Yogurt", 9, 4, 10);
        products.add(newProduct);

        // שלב 3: שומר את הרשימה המורחבת
        dao.saveProducts(products);

        // שלב 4: טוען מחדש כדי לבדוק שהמוצר באמת נשמר
        List<Product> updatedProducts = dao.loadProducts();
        System.out.println("\nAfter:");
        for (Product p : updatedProducts) {
            System.out.println(p.getName() + " (stock: " + p.getStock() + ")");
        }
    }
}
