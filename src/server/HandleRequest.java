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
                        added ? "Product added successfully" : "Product already exists",
                        null);

            case "DELETE":
                service.removeProduct(request.getTargetName());
                return new Response(true, "Product deleted", null);

            case "GET_ALL":
                List<Product> products = service.getAllProducts();
                return new Response(true, "Product list returned", products);

            //case "RECOMMEND_LOW":
            //    List<Product> low = service.recommendLowStock();
            //    return new Response(true, "Low stock recommendation", low);

            //case "RECOMMEND_PROFIT":
           //     List<Product> high = service.recommendHighProfit();
            //    return new Response(true, "High profit recommendation", high);

            default:
                return new Response(false, "Unknown action: " + action, null);
        }
    }
}
