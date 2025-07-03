package test;

import client.socket.ClientSocket;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class TestClientAllActions {
    public static void main(String[] args) {
        ClientSocket client = new ClientSocket();

        // ===== 1. הוספת מוצר חדש =====
        Product newProduct = new Product("TestProduct", 20, 10, 15);
        Request addRequest = new Request(newProduct,"ADD", null);
        Response addResponse = client.sendRequest(addRequest);
        System.out.println("ADD Response: " + addResponse.getMessage());

        // ===== 2. שליפת כל המוצרים =====
        Request getAllRequest = new Request(null, "GET_ALL", null);
        Response getAllResponse = client.sendRequest(getAllRequest);
        System.out.println("\nGET_ALL Response: " + getAllResponse.getMessage());
        List<Product> allProducts = getAllResponse.getData();
        if (allProducts != null) {
            for (Product p : allProducts) {
                System.out.println(p);
            }
        }

        // ===== 3. מחיקת מוצר =====
        Request deleteRequest = new Request(null, "DELETE", "TestProduct");
        Response deleteResponse = client.sendRequest(deleteRequest);
        System.out.println("\nDELETE Response: " + deleteResponse.getMessage());

        // ===== 4. שליפת כל המוצרים שוב לוודא שנמחק =====
        Response afterDelete = client.sendRequest(getAllRequest);
        System.out.println("\nGET_ALL (after delete): " + afterDelete.getMessage());
        List<Product> updatedProducts = afterDelete.getData();
        if (updatedProducts != null) {
            for (Product p : updatedProducts) {
                System.out.println(p);
            }
        }
    }
}
