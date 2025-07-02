package dao;

import model.Product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileDaoImpl implements IDao{
    @Override
    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("resources/datasource.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int sellPrice = Integer.parseInt(parts[1]);
                    int buyPrice = Integer.parseInt(parts[2]);
                    int stock = Integer.parseInt(parts[3]);

                    Product p = new Product(name, sellPrice, buyPrice, stock);
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
    @Override
    public void saveProducts(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/datasource.txt"))) {
            for (Product p : products) {
                String line = p.getName() + "," +
                        p.getSellingPrice() + "," +
                        p.getBuyingPrice() + "," +
                        p.getStock();
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
