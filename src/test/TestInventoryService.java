package test;

import dao.FileDaoImpl;
import model.Product;
import service.InventoryService;

import java.util.List;

public class TestInventoryService {
    public static void main(String[] args) {
        InventoryService service = new InventoryService(new FileDaoImpl());

        // 1. הצגת מוצרים קיימים
        System.out.println("Before:");
        List<Product> products = service.getAllProducts();
        for (Product p : products) {
            System.out.println(p.toString());
        }

        // 2. הוספת מוצר חדש
        Product yogurt = new Product("Car", 9, 4, 10);
        service.addProduct(yogurt);

        // 3. עדכון מוצר קיים (שינוי מחירים ומלאי)
        Product updatedYogurt = new Product("Milk", 13, 5, 20);
        service.UpdateProduct(updatedYogurt);

        // 4. מחיקת מוצר אחר
        service.removeProduct("Eggs");

        // 5. טעינה מחדש והצגת תוצאה
        System.out.println("\nAfter:");
        List<Product> updated = service.getAllProducts();
        for (Product p : updated) {
            System.out.println(p.toString());
        }
    }
}
