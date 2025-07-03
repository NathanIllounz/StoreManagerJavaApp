package client.ui;

import client.socket.ClientSocket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class ClientApp extends Application {

    private TextArea outputArea;
    private ClientSocket client;

    @Override
    public void start(Stage primaryStage) {
        client = new ClientSocket(); // יצירת אובייקט לקוח

        // יצירת רכיבי GUI
        Button getAllBtn = new Button("Get All Products");
        Button recommendLowBtn = new Button("Low Stock Recommendation");
        Button recommendProfitBtn = new Button("High Profit Recommendation");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // אירוע כשלוחצים על הכפתור Get All
        getAllBtn.setOnAction(e -> getAllProducts());

        recommendLowBtn.setOnAction(e -> sendRequest("RECOMMEND_LOW"));
        recommendProfitBtn.setOnAction(e -> sendRequest("RECOMMEND_PROFIT"));

        // בניית הממשק
        VBox layout = new VBox(10, getAllBtn, recommendLowBtn, recommendProfitBtn, outputArea);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Store Manager Client");
        primaryStage.show();
    }

    private void getAllProducts() {
        Request req = new Request("GET_ALL", null, null);
        Response res = client.sendRequest(req);

        if (res.isSuccess()) {
            StringBuilder sb = new StringBuilder(res.getMessage() + "\n");
            List<Product> products = res.getData();
            for (Product p : products) {
                sb.append(p).append("\n");
            }
            outputArea.setText(sb.toString());
        } else {
            outputArea.setText("Error: " + res.getMessage());
        }
    }

    private void sendRequest(String action) {
        Request req = new Request(action, null, null);
        Response res = client.sendRequest(req);
        if (res.isSuccess()) {
            StringBuilder sb = new StringBuilder(res.getMessage() + "\n");
            List<Product> products = res.getData();
            for (Product p : products) {
                sb.append(p).append("\n");
            }
            outputArea.setText(sb.toString());
        } else {
            outputArea.setText("Error: " + res.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(); // מריץ את ה־JavaFX
    }
}
