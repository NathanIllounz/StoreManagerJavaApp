package client.ui;

import client.socket.ClientSocket;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

        nameField.setMaxWidth(300);
        buyField.setMaxWidth(300);
        sellField.setMaxWidth(300);
        stockField.setMaxWidth(300);

        addButton.setOnAction(e -> {
            boolean hasError = false;
            if (nameField.getText().isBlank()) { nameField.setStyle("-fx-border-color: red;"); hasError = true; }
            else nameField.setStyle("");
            if (buyField.getText().isBlank()) { buyField.setStyle("-fx-border-color: red;"); hasError = true; }
            else buyField.setStyle("");
            if (sellField.getText().isBlank()) { sellField.setStyle("-fx-border-color: red;"); hasError = true; }
            else sellField.setStyle("");
            if (stockField.getText().isBlank()) { stockField.setStyle("-fx-border-color: red;"); hasError = true; }
            else stockField.setStyle("");

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

        HBox buttonBox = new HBox(addButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Buy Price:"), buyField,
                new Label("Sell Price:"), sellField,
                new Label("Stock:"), stockField,
                buttonBox
        );
        form.setPadding(new Insets(30));
        form.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox(form);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefWidth(400);
        wrapper.setMaxWidth(400);

        HBox container = new HBox(wrapper);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Pane buildProductTableTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Search product by name...");
        searchField.setMaxWidth(300);

        Button searchBtn = new Button("Search");
        Button resetBtn = new Button("Reset Table");
        Button editBtn = new Button("Edit Selected");
        Button deleteBtn = new Button("Delete Selected");

        HBox searchBox = new HBox(10, searchField, searchBtn, resetBtn);
        searchBox.setAlignment(Pos.CENTER);

        tableView = new TableView<>();
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, Integer> buyCol = new TableColumn<>("Buy Price");
        buyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBuyingPrice()).asObject());

        TableColumn<Product, Integer> sellCol = new TableColumn<>("Sell Price");
        sellCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSellingPrice()).asObject());

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());

        tableView.getColumns().addAll(nameCol, buyCol, sellCol, stockCol);

        productList = FXCollections.observableArrayList();
        Response res = ClientSocket.sendRequest(new Request(null, "GET_ALL", null));
        if (res != null && res.getData() != null) {
            productList.addAll(res.getData());
        }
        tableView.setItems(productList);

        searchBtn.setOnAction(e -> {
            String name = searchField.getText();
            if (name == null || name.isBlank()) {
                showAlert("Search", "Please enter a product name.");
                return;
            }
            Response searchRes = ClientSocket.sendRequest(new Request(null, "SEARCH", name));
            if (searchRes.isSuccess() && searchRes.getData() != null && !searchRes.getData().isEmpty()) {
                tableView.setItems(FXCollections.observableArrayList(searchRes.getData()));
            } else {
                showAlert("Search", "Product not found.");
            }
        });

        resetBtn.setOnAction(e -> {
            Response resetRes = ClientSocket.sendRequest(new Request(null, "GET_ALL", null));
            if (resetRes != null && resetRes.getData() != null) {
                tableView.setItems(FXCollections.observableArrayList(resetRes.getData()));
            }
        });

        editBtn.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Edit", "Please select a product first.");
                return;
            }

            // Create popup
            Dialog<Product> dialog = new Dialog<>();
            dialog.setTitle("Edit Product");

            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField(selected.getName());
            nameField.setDisable(true);

            Label buyLabel = new Label("Buy Price:");
            TextField buyField = new TextField(String.valueOf(selected.getBuyingPrice()));

            Label sellLabel = new Label("Sell Price:");
            TextField sellField = new TextField(String.valueOf(selected.getSellingPrice()));

            Label stockLabel = new Label("Stock:");
            TextField stockField = new TextField(String.valueOf(selected.getStock()));

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(nameLabel, 0, 0); grid.add(nameField, 1, 0);
            grid.add(buyLabel, 0, 1); grid.add(buyField, 1, 1);
            grid.add(sellLabel, 0, 2); grid.add(sellField, 1, 2);
            grid.add(stockLabel, 0, 3); grid.add(stockField, 1, 3);

            dialog.getDialogPane().setContent(grid);
            ButtonType updateBtnType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateBtnType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateBtnType) {
                    try {
                        Product updated = new Product(
                                selected.getName(),
                                Integer.parseInt(sellField.getText()),
                                Integer.parseInt(buyField.getText()),
                                Integer.parseInt(stockField.getText()));
                        return updated;
                    } catch (Exception ex) {
                        showAlert("Edit Error", "Invalid input");
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updated -> {
                Response updateRes = ClientSocket.sendRequest(new Request(updated, "UPDATE", null));
                showAlert("Update", updateRes.getMessage());
                resetBtn.fire();
            });
        });

        deleteBtn.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Delete", "Please select a product to delete.");
                return;
            }
            Response resDel = ClientSocket.sendRequest(new Request(null, "DELETE", selected.getName()));
            showAlert("Delete", resDel.getMessage());
            resetBtn.fire();
        });

        HBox actionBox = new HBox(10, editBtn, deleteBtn);
        actionBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(searchBox, tableView, actionBox);
        return layout;
    }


    private Pane buildRecommendationTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        TextField limitField = new TextField();
        limitField.setPromptText("Number of products (e.g. 2 / 5 / 10 or 'all')");
        limitField.setMaxWidth(300);

        Button lowStockBtn = new Button("Recommend Low Stock");
        Button highProfitBtn = new Button("Recommend High Profit");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefWidth(400);
        resultArea.setWrapText(true);

        HBox buttons = new HBox(10, lowStockBtn, highProfitBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(10,
                new Label("How many products to return:"),
                limitField,
                buttons,
                resultArea
        );
        content.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox(content);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefWidth(400);

        HBox container = new HBox(wrapper);
        container.setAlignment(Pos.CENTER);

        lowStockBtn.setOnAction(e -> handleRecommendation("RECOMMEND_LOW", limitField.getText(), resultArea));
        highProfitBtn.setOnAction(e -> handleRecommendation("RECOMMEND_PROFIT", limitField.getText(), resultArea));

        return container;
    }

    private void handleRecommendation(String action, String limitText, TextArea area) {
        Response res = ClientSocket.sendRequest(new Request(null, action, limitText));
        if (res.isSuccess() && res.getData() != null) {
            StringBuilder sb = new StringBuilder();
            for (Product p : res.getData()) {
                int profit = p.getSellingPrice() - p.getBuyingPrice();
                sb.append("Product: ").append(p.getName());
                if (action.equals("RECOMMEND_PROFIT")) {
                    sb.append(" | Expected Profit: ").append(profit);
                } else {
                    sb.append(" | Stock: ").append(p.getStock());
                }
                sb.append("\n");
            }
            area.setText(sb.toString());
        } else {
            area.setText("No recommendations available.");
        }
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
