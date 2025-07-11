package network;

import model.Product;
import java.io.Serializable;


public class Request implements Serializable{

    private Product product;
    private String action;
    private String targetName;

    public Request(Product product, String action, String targetName) {
        this.product = product;
        this.action = action;
        this.targetName = targetName;
    }
    public String getAction() {
        return action;
    }
    public Product getProduct() {
        return product;
    }
    public String getTargetName() {
        return targetName;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", product=" + product +
                ", targetName='" + targetName + '\'' +
                '}';
    }
}
