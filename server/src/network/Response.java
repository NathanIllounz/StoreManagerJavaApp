package network;

import model.Product;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private boolean success;
    private String message;
    private List<Product> data;

    public Response( boolean success, String message, List<Product> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public List<Product> getData() {
        return data;
    }
    public List<Product> getProducts() {return data;}
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setData(List<Product> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
