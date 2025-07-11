package server;

import network.Request;
import network.Response;
import service.InventoryService;
import dao.FileDaoImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 1234;

    public static void main(String[] args) {
        InventoryService service = new InventoryService(new FileDaoImpl());
        HandleRequest handler = new HandleRequest(service);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                try (
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
                ) {
                    // קבלת הבקשה
                    Request request = (Request) in.readObject();
                    System.out.println("Received request: " + request);

                    // טיפול בבקשה
                    Response response = handler.handle(request);

                    // שליחת תשובה
                    out.writeObject(response);
                    System.out.println("Response sent: " + response);

                } catch (Exception e) {
                    System.err.println("Error handling client: " + e.getMessage());
                } finally {
                    clientSocket.close();
                    System.out.println("Client disconnected");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}