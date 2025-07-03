package client.socket;

import network.Request;
import network.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public Response sendRequest(Request request) {
        try (
                Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // שליחת בקשה
            out.writeObject(request);
            out.flush();

            // קבלת תשובה
            Response response = (Response) in.readObject();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Client error: " + e.getMessage(), null);
        }
    }
}
