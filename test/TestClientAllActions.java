package test;

import client.socket.ClientSocket;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class TestClientAllActions {
    public static void main(String[] args) {

        // 1. הוספת מוצר
        Product p = new Product("TestProduct", 20, 10, 15);
        Response addRes = ClientSocket.sendRequest(new Request(null, "ADD", null));
        System.out.println("ADD Response: " + addRes.getMessage());

        // 2. שליפת מוצרים
        Response allRes = ClientSocket.sendRequest(new Request(null, "GET_ALL", null));
        System.out.println("\nGET_ALL Response: " + allRes.getMessage());
        if (allRes.getProducts() != null) {
            for (Product prod : allRes.getProducts()) {
                System.out.println(prod);
            }
        }

        // 3. מחיקה
        Response delRes = ClientSocket.sendRequest(new Request(null, "DELETE", "TestProduct"));
        System.out.println("\nDELETE Response: " + delRes.getMessage());

        // 4. בדיקת אלגוריתם מלאי נמוך
        Response lowStockRes = ClientSocket.sendRequest(new Request(null, "RECOMMEND_LOW", null));
        System.out.println("\nLOW STOCK Recommendation:");
        if (lowStockRes.getProducts() != null) {
            for (Product prod : lowStockRes.getProducts()) {
                System.out.println(prod.getName() + " (stock: " + prod.getStock() + ")");
            }
        }
    }
}
