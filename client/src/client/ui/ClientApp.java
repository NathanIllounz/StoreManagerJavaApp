package client.ui;

import client.socket.ClientSocket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class ClientApp extends Application {

    private TextArea outputArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab addTab = new Tab("Add Product", buildAddProductTab());
        Tab getAllTab = new Tab("Get All Products", buildGetAllTab());
        Tab searchTab = new Tab("Search Product", buildSearchTab());
        Tab recTab = new Tab("Recommendations", buildRecommendationTab());


        tabPane.getTabs().addAll(addTab, getAllTab, searchTab, recTab);

        outputArea.setEditable(false);
        VBox root = new VBox(tabPane, outputArea);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Store Manager Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane buildAddProductTab() {
        TextField nameField = new TextField();
        TextField buyField = new TextField();
        TextField sellField = new TextField();
        TextField stockField = new TextField();
        Button addButton = new Button("Add");

        addButton.setOnAction(e -> {
            try {
                Product p = new Product(
                        nameField.getText(),
                        Integer.parseInt(sellField.getText()),
                        Integer.parseInt(buyField.getText()),
                        Integer.parseInt(stockField.getText())
                );
                Response res = ClientSocket.sendRequest(new Request(p, "ADD", null));
                showAlert("Add Product", res.getMessage());
            } catch (Exception ex) {
                outputArea.setText("Invalid input.");
            }
        });

        VBox box = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Buy Price:"), buyField,
                new Label("Sell Price:"), sellField,
                new Label("Stock:"), stockField,
                addButton
        );
        return box;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Pane buildRecommendationTab() {
        VBox layout = new VBox(10);

        // שדה להזנת מספר מוצרים להחזיר
        TextField limitField = new TextField();
        limitField.setPromptText("Number of products (e.g. 2 / 5 / 10 or 'all')");

        // כפתורים
        Button lowStockBtn = new Button("Recommend Low Stock");
        Button highProfitBtn = new Button("Recommend High Profit");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // פעולה כללית עם פרמטרים
        lowStockBtn.setOnAction(e -> {
            handleRecommendation("RECOMMEND_LOW", limitField.getText(), resultArea);
        });

        highProfitBtn.setOnAction(e -> {
            handleRecommendation("RECOMMEND_PROFIT", limitField.getText(), resultArea);
        });

        layout.getChildren().addAll(
                new Label("How many products to return:"),
                limitField,
                lowStockBtn,
                highProfitBtn,
                resultArea
        );

        return layout;
    }

    private void handleRecommendation(String action, String limitInput, TextArea outputArea) {
        Response res = ClientSocket.sendRequest(new Request(null, action, null));
        if (!res.isSuccess() || res.getProducts() == null) {
            outputArea.setText("Failed to fetch recommendation: " + res.getMessage());
            return;
        }

        List<Product> products = res.getProducts();

        // סינון לפי מספר תוצאות (אם לא "all")
        if (!limitInput.isBlank() && !limitInput.equalsIgnoreCase("all")) {
            try {
                int limit = Integer.parseInt(limitInput.trim());
                if (limit < products.size()) {
                    products = products.subList(0, limit);
                }
            } catch (Exception ex) {
                outputArea.setText("Invalid limit. Please enter a number or 'all'.");
                return;
            }
        }

        // הדפסת התוצאות
        StringBuilder sb = new StringBuilder();
        if (action.equals("RECOMMEND_LOW")) {
            sb.append("Low Stock Recommendation:\n");
            for (Product p : products) {
                sb.append("- ").append(p.getName())
                        .append(" (stock: ").append(p.getStock()).append(")\n");
            }
        } else {
            sb.append("High Profit Recommendation:\n");
            for (Product p : products) {
                int profit = p.getSellingPrice() - p.getBuyingPrice();
                sb.append("- ").append(p.getName())
                        .append(" (profit: ").append(profit).append(")\n");
            }
        }

        outputArea.setText(sb.toString());
    }

    private Pane buildGetAllTab() {
        Button getAllButton = new Button("Refresh Products");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        getAllButton.setOnAction(e -> {
            Response res = ClientSocket.sendRequest(new Request(null, "GET_ALL", null));
            if (res.isSuccess() && res.getProducts() != null) {
                StringBuilder sb = new StringBuilder("Products:\n");
                for (Product p : res.getProducts()) {
                    sb.append("- ").append(p.getName())
                            .append(" | Buy: ").append(p.getBuyingPrice())
                            .append(" | Sell: ").append(p.getSellingPrice())
                            .append(" | Stock: ").append(p.getStock())
                            .append("\n");
                }
                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("Failed to fetch products: " + res.getMessage());
            }
        });

        VBox layout = new VBox(10, getAllButton, resultArea);
        return layout;
    }

    private Pane buildSearchTab() {
        VBox layout = new VBox(10);

        TextField searchField = new TextField();
        Button searchButton = new Button("Search");

        TextField buyField = new TextField();
        TextField sellField = new TextField();
        TextField stockField = new TextField();

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        Label statusLabel = new Label();

        // התחלה - הכל כבוי
        buyField.setDisable(true);
        sellField.setDisable(true);
        stockField.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        searchButton.setOnAction(e -> {
            String name = searchField.getText();
            if (name.isBlank()) {
                statusLabel.setText("Please enter a product name.");
                return;
            }

            Response res = ClientSocket.sendRequest(new Request(null, "SEARCH", name));
            if (res.isSuccess() && res.getProducts() != null && !res.getProducts().isEmpty()) {
                Product p = res.getProducts().get(0);
                buyField.setText(String.valueOf(p.getBuyingPrice()));
                sellField.setText(String.valueOf(p.getSellingPrice()));
                stockField.setText(String.valueOf(p.getStock()));

                buyField.setDisable(false);
                sellField.setDisable(false);
                stockField.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);

                statusLabel.setText("Product found: " + p.getName());
            } else {
                statusLabel.setText("Product not found.");
                buyField.setDisable(true);
                sellField.setDisable(true);
                stockField.setDisable(true);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        updateButton.setOnAction(e -> {
            try {
                Product updated = new Product(
                        searchField.getText(),
                        Integer.parseInt(sellField.getText()),
                        Integer.parseInt(buyField.getText()),
                        Integer.parseInt(stockField.getText())
                );
                Response res = ClientSocket.sendRequest(new Request(updated, "UPDATE", null));
                showAlert("Update", res.getMessage());
            } catch (Exception ex) {
                statusLabel.setText("Invalid input.");
            }
        });

        deleteButton.setOnAction(e -> {
            String name = searchField.getText();
            if (name.isBlank()) {
                statusLabel.setText("No product selected.");
                return;
            }
            Response res = ClientSocket.sendRequest(new Request(null, "DELETE", name));
            showAlert("Delete", res.getMessage());

            // נקה את השדות
            buyField.clear(); sellField.clear(); stockField.clear();
            buyField.setDisable(true); sellField.setDisable(true); stockField.setDisable(true);
            updateButton.setDisable(true); deleteButton.setDisable(true);
        });

        layout.getChildren().addAll(
                new Label("Search product by name:"), searchField, searchButton,
                new Label("Buy Price:"), buyField,
                new Label("Sell Price:"), sellField,
                new Label("Stock:"), stockField,
                new HBox(10, updateButton, deleteButton),
                statusLabel
        );

        return layout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
