package client.ui;

import client.socket.ClientSocket;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Product;
import network.Request;
import network.Response;

import java.util.List;

public class ClientApp extends Application {

    private TableView<Product> tableView;
    private ObservableList<Product> productList;

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab addTab = new Tab("Add Product", buildAddProductTab());
        Tab getAllTab = new Tab("Manage Products", buildProductTableTab());
        Tab recommendationTab = new Tab("Recommendations", buildRecommendationTab());

        tabPane.getTabs().addAll(addTab, getAllTab, recommendationTab);

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 800, 600);
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
            boolean hasError = false;
            if (nameField.getText().isBlank()) {
                nameField.setStyle("-fx-border-color: red;"); hasError = true;
            } else nameField.setStyle("");
            if (buyField.getText().isBlank()) {
                buyField.setStyle("-fx-border-color: red;"); hasError = true;
            } else buyField.setStyle("");
            if (sellField.getText().isBlank()) {
                sellField.setStyle("-fx-border-color: red;"); hasError = true;
            } else sellField.setStyle("");
            if (stockField.getText().isBlank()) {
                stockField.setStyle("-fx-border-color: red;"); hasError = true;
            } else stockField.setStyle("");

            if (hasError) {
                showAlert("Add Product Error", "Please fill in all fields.");
                return;
            }

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
                showAlert("Invalid Input", "Please enter valid numbers.");
            }
        });

        return new VBox(10,
                new Label("Name:"), nameField,
                new Label("Buy Price:"), buyField,
                new Label("Sell Price:"), sellField,
                new Label("Stock:"), stockField,
                addButton
        );
    }

    private Pane buildProductTableTab() {
        VBox layout = new VBox(10);

        tableView = new TableView<>();
        productList = FXCollections.observableArrayList();

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Product, Integer> buyCol = new TableColumn<>("Buy Price");
        buyCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getBuyingPrice()).asObject());

        TableColumn<Product, Integer> sellCol = new TableColumn<>("Sell Price");
        sellCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getSellingPrice()).asObject());

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getStock()).asObject());

        tableView.getColumns().addAll(nameCol, buyCol, sellCol, stockCol);
        tableView.setItems(productList);

        Button refreshButton = new Button("Refresh");
        Button editButton = new Button("Edit Selected");
        Button deleteButton = new Button("Delete Selected");

        refreshButton.setOnAction(e -> refreshProducts());

        editButton.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Edit", "Please select a product to edit.");
                return;
            }
            showEditDialog(selected);
        });

        deleteButton.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Delete", "Please select a product to delete.");
                return;
            }
            Response res = ClientSocket.sendRequest(new Request(null, "DELETE", selected.getName()));
            showAlert("Delete", res.getMessage());
            refreshProducts();
        });

        layout.getChildren().addAll(new HBox(10, refreshButton, editButton, deleteButton), tableView);
        refreshProducts();
        return layout;
    }

    private void refreshProducts() {
        Response res = ClientSocket.sendRequest(new Request(null, "GET_ALL", null));
        if (res.isSuccess()) {
            productList.setAll(res.getProducts());
        } else {
            showAlert("Error", "Failed to load products: " + res.getMessage());
        }
    }

    private void showEditDialog(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product: " + product.getName());

        TextField buyField = new TextField(String.valueOf(product.getBuyingPrice()));
        TextField sellField = new TextField(String.valueOf(product.getSellingPrice()));
        TextField stockField = new TextField(String.valueOf(product.getStock()));

        VBox content = new VBox(10,
                new Label("Buy Price:"), buyField,
                new Label("Sell Price:"), sellField,
                new Label("Stock:"), stockField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    Product updated = new Product(
                            product.getName(),
                            Integer.parseInt(sellField.getText()),
                            Integer.parseInt(buyField.getText()),
                            Integer.parseInt(stockField.getText())
                    );
                    return updated;
                } catch (Exception e) {
                    showAlert("Error", "Invalid input.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedProduct -> {
            Response res = ClientSocket.sendRequest(new Request(updatedProduct, "UPDATE", null));
            showAlert("Update", res.getMessage());
            refreshProducts();
        });
    }

    private Pane buildRecommendationTab() {
        VBox layout = new VBox(10);

        TextField limitField = new TextField();
        limitField.setPromptText("Number of products (e.g. 2 / 5 / 10 or 'all')");

        Button lowStockBtn = new Button("Recommend Low Stock");
        Button highProfitBtn = new Button("Recommend High Profit");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        lowStockBtn.setOnAction(e -> handleRecommendation("RECOMMEND_LOW", limitField.getText(), resultArea));
        highProfitBtn.setOnAction(e -> handleRecommendation("RECOMMEND_PROFIT", limitField.getText(), resultArea));

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
