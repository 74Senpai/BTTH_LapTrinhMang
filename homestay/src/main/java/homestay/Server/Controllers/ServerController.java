package homestay.Server.Controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.LogService;

public class ServerController {

    private ServerSocket serverSocket;
    private final int port;
    private final Gson gson = new Gson();
    private volatile boolean isActivate = false;

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
            LogService.writeLog("Listen Client ERROR: " + e.getMessage(), "Unknown", new Date());
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
        if (req.dir().equals("AUTH") && req.action().equals("LOGIN")) {
            return NhanVienController.login(req);
        }
        boolean isLogin = AuthController.isAuthenticated(req.session());
        if (!isLogin) {
            return DataBuilder.unAuthRes(req);
        }

        System.out.println("Request: " + request);
        String userName = AuthController.getUsername(req.session());
        LogService.writeLog(request, userName, new Date());

        switch (req.dir()) {
            case "BASE" -> {
                return BaseDataController.baseDataController(req);
            }
            case "ROOM" -> {
                return PhongController.phongController(req);
            }
            case "HOP_DONG" -> {
                return HopDongController.hopDongController(req);
            }
            case "DIEN_NUOC" -> {
                return DienNuocController.dienNuocController(req);
            }
            case "HOA_DON" -> {
                return HoaDonController.hoaDonController(req);
            }
            default -> {
                return DataBuilder.notFoundRes(req);
            }
        }
    }
}
