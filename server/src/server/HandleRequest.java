package server;

import model.Product;
import network.Request;
import network.Response;
import service.InventoryService;

import java.util.List;

public class HandleRequest {

    private InventoryService service;

    public HandleRequest(InventoryService service) {
        this.service = service;
    }

    public Response handle(Request request) {
        String action = request.getAction();

        switch (action) {
            case "ADD":
                Product p = request.getProduct();
                boolean added = service.addProduct(p); // תעדכן שתחזיר בוליאני
                return new Response(added,
                        added ? "Product '" + p.getName() + "' added successfully"
                                : "Product '" + p.getName() + "' already exists",
                        null);

            case "DELETE":
                service.deleteProduct(request.getTargetName());
                String name = request.getTargetName();
                service.deleteProduct(name);
                return new Response(true, "Product '" + name + "' deleted successfully", null);

            case "GET_ALL":
                List<Product> products = service.getAllProducts();
                return new Response(true, "Product list returned", products);

            case "RECOMMEND_LOW":
                List<Product> low = service.recommendLowStock();
                String stockMsg = low.isEmpty()
                        ? "No products found with low stock."
                        : "Recommended to restock '" + low.get(0).getName() + "' — only " +
                        low.get(0).getStock() + " units left in stock.";
                return new Response(true, stockMsg, low);

            case "RECOMMEND_PROFIT":
                List<Product> high = service.recommendHighProfit();
                String profitMsg = high.isEmpty()
                        ? "No products found for high profit recommendation."
                        : "Recommended to sell '" + high.get(0).getName() + "' — expected profit is " +
                        (high.get(0).getSellingPrice() - high.get(0).getBuyingPrice()) + " per unit.";
                return new Response(true, profitMsg, high);

            default:
                return new Response(false, "Unknown action: " + action, null);
        }
    }
}
