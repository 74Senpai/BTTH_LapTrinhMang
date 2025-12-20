package homestay.Server.Controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.DTOs.NhanVienDTO;
import homestay.Server.Services.NhanVienService;
import homestay.Server.Services.PhongService;
import homestay.Server.Services.TrangThaiPhongService;

public class ServerController {

    private ServerSocket serverSocket;
    private final int port;
    private final Gson gson = new Gson();
    private volatile boolean isActivate = false;
    private Map<String, String> sessionMap = new ConcurrentHashMap<>();;

    public ServerController(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.isActivate = true;
            System.out.println("Khởi tạo server socket thành công");
        } catch (IOException e) {
            System.err.println("Không thể khởi động socket server: " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            this.isActivate = false;
            this.serverSocket.close();
            System.out.println("Đóng kết nối thành công");
        } catch (IOException e) {
            System.out.println("Đóng kết nối thất bại: " + e.getMessage());
        }
    }

    public void startListening() {
        while (this.isActivate) {
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

        if (req.getAction().equals("LOGIN")) {
            NhanVienDTO.Login loginInfor = gson.fromJson(req.getData(), NhanVienDTO.Login.class);
            NhanVienDTO.LoginStatus status = new NhanVienService().checkLogin(loginInfor);
            if(status.isLogin()){
                sessionMap.put(status.getSession(), loginInfor.getUsername());
                System.out.println("User "+loginInfor.getUsername()+" đăng nhập thành công!");
            }
            return this.buildResponse(
                    req.getAction(),
                    200,
                    "OKE",
                    gson.toJsonTree(status)
            );
        }

        if (!sessionMap.containsKey(req.getSession())) {
            return this.buildResponse(req.getAction(), 403, "Unauthorized", null);
        }

        switch (req.getAction()) {
            case "GET_ROOM_STATES" -> {
                return this.buildResponse(
                    req.getAction(),
                    200,
                    "OKE",
                    gson.toJsonTree(new TrangThaiPhongService().getAllTrangThai())  
                );
            }
            case "GET_ROOMS" -> {
                return this.buildResponse(
                    req.getAction(),
                    200,
                    "OKE",
                    gson.toJsonTree(new PhongService().getAllPhong()) 
                );
            }
            default -> {
                return this.buildResponse(
                        req.getAction(),
                        400,
                        "Invalid request",
                        null
                );
            }
        }
    }

    private String buildResponse(String action, int statusCode, String message, JsonElement data) {
        BaseDTO.Response res = new BaseDTO.Response(
                action,
                statusCode,
                message,
                data
        );
        return gson.toJson(res);
    }
}
