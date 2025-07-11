package client.ui;

import client.socket.ClientSocket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class ClientApp extends Application {

    private TextArea output;

    @Override
    public void start(Stage primaryStage) {
        output = new TextArea();
        output.setEditable(false);

        Button getAllBtn = new Button("Get All Products");
        getAllBtn.setOnAction(e -> getAllProducts());

        VBox layout = new VBox(10, getAllBtn, output);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setTitle("Store Manager Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void getAllProducts() {
        Request req = new Request(null, "GET_ALL", null);
        Response res = ClientSocket.sendRequest(req);

        if (res != null && res.isSuccess()) {
            List<Product> products = res.getProducts();
            StringBuilder sb = new StringBuilder("Products:\n");
            for (Product p : products) {
                sb.append("- ").append(p.getName()).append(" (stock: ").append(p.getStock()).append(")\n");
            }
            output.setText(sb.toString());
        } else {
            output.setText("Failed to fetch products.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
