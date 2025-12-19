package homestay.Server.Controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import homestay.Server.DTOs.BaseDTO;

public class ServerSocketController {

    private ServerSocket serverSocket;
    private final int port;
    private final int SO_TIMEOUT = 30000;
    private final Gson gson = new Gson();

    public ServerSocketController(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.serverSocket.setSoTimeout(this.SO_TIMEOUT);
            System.out.println("Khởi tạo server socket thành công");
        } catch (IOException e) {
            System.err.println("Không thể khởi động socket server: " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            this.serverSocket.close();
            System.out.println("Đóng kết nối thành công");
        } catch (IOException e) {
            System.out.println("Đóng kết nối thất bại: " + e.getMessage());
        }
    }

    public void startListening() {
        while (true) {
            listenClient();
        }
    }

    public void listenClient() {
        try {
            Socket clientSocket = this.serverSocket.accept();
            System.out.println("Client accepted: " + clientSocket.getInetAddress());

            new Thread(() -> handleClient(clientSocket)).start();

        } catch (IOException e) {
            System.err.println("Lỗi khi lắng nghe Client: " + e.getMessage());
        }
    }

    public void handleClient(Socket clientSocket) {
        try (
                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(clientSocket.getInputStream())
                ); DataOutputStream out = new DataOutputStream(
                        clientSocket.getOutputStream()
                )) {
            while (true) {
                String request = in.readUTF();
                String response = switchRequest(request);
                out.writeUTF(response);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private String switchRequest(String request) {
        BaseDTO.Request req = gson.fromJson(request, BaseDTO.Request.class); 
        switch (req.getAction()) {
            case "LOGIN" -> {
                return "You are login";
            }
            case "GET_ROOM" -> {
                return "Room";
            }
            default -> {
                BaseDTO.Response res = new BaseDTO.Response(
                    400, 
                    "Invalid request", 
                    null
                ); 
                return gson.toJson(res);
            }
        }
    }

}
