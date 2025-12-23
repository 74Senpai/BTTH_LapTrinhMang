package homestay.Client.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import homestay.Client.Helper.SessionManager;
import homestay.DTOs.BaseDTO;

public class ClientSocketController {

    private static Socket socket;
    private static String domain;
    private static int port;
    private static final Gson gson = new Gson();
    private static DataInputStream inputStream = null;
    private static final int SO_TIMEOUT = 10000;

    private ClientSocketController() {}

    public static void serverURL(String address, int p){
        domain = address;
        port = p;
    }

    public static void openConnection() throws Exception {
        try {
            socket = new Socket(domain, port);
            socket.setSoTimeout(SO_TIMEOUT);
        } catch (UnknownHostException e) {
            System.err.println("Địa chỉ hoặc cổng không hợp lệ: " + e.getMessage());
            throw new Exception("Địa chỉ hoặc cổng không hợp lệ: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối tới server: " + e.getMessage());
            throw new Exception("Lỗi khi kết nối tới server: " + e.getMessage());
        } finally {
            if (!isConnected()) {
                System.err.println("Kết nối tới server thất bại!!!");
            } else {
                System.out.println("Kết nối tới server thành công!");
            }
        }
    }

    public static void closeConnection() throws Exception {
        if (isConnected()) {
            socket.close();
            System.out.println("Đã ngắt kết nối tới server");
        } else {
            System.out.println(" Không có kết nối nào tới server");
        }
    }

    public static void sendMessage(String dir, String action, Object mess, boolean fallback) {
        if (!ensureConnected()) {
            System.err.println("Gửi message không thành công! \n Message:" + action + ":" + mess);
            return;
        }
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            String jsonMess = buildMessage(dir, action, mess);
            outputStream.writeUTF(jsonMess);
            System.out.println("Gửi tin tới server thành công");
        } catch (IOException e) {
            System.err.println("Không thể gửi yêu cầu tới server: " + e.getMessage());
            if (fallback) {
                System.out.println("Tiến hành thử lại.");
                sendMessage(dir, action, mess, false);
            }
        }
    }

    public static void initListener() {
        if (!ensureConnected()) {
            System.err.println("Không thể khởi tạo Listener");
            return;
        }
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println("Khởi tạo listener thành công!");
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo Listener: " + e.getMessage());
        }
    }

    public static String listenMessage() throws Exception {
        if (inputStream == null) {
            throw new IllegalStateException("Listener chưa được khởi tạo. Gọi initListener() trước.");
        }
        return inputStream.readUTF();
    }

    public static BaseDTO.Response sendRequest(String dir, String action, Object data, boolean fallback) {
        if (!ensureConnected()) {
            System.err.println("Không có kết nối tới server. Gửi request thất bại!");
            return null;
        }

        if (inputStream == null) {
            initListener();
        }

        try {
            sendMessage(dir, action, data, fallback);
            System.out.println("Đợi phản hồi từ server...");
            String responseJson = listenMessage();
            System.out.println("Đã nhận phản hồi từ server");
            return gson.fromJson(responseJson, BaseDTO.Response.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Lỗi khi parser dữ liệu từ server: " + e.getMessage());
            return null;
        } catch (Exception e){
            System.err.println("Lỗi khi gửi request: " + e.getMessage());
            return null;
        }
    }

    public static void closeListener() throws Exception {
        if (inputStream != null) {
            inputStream.close();
            System.out.println("Đóng listenner");
        }
    }

    public static void kill() {
        try {
            closeListener();
            closeConnection();
            System.out.println("Đã dừng Socket");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String buildMessage(String dir, String action, Object data) {
        BaseDTO.Request request = new BaseDTO.Request(
            dir, action, gson.toJsonTree(data), SessionManager.getSession());
        return gson.toJson(request);
    }

    public static boolean ensureConnected() {
        if(socket != null){
            if (socket.isConnected()) {
                return true;
            }
        }
        
        System.err.println("Không có kết nối tới server");
        System.out.println("Thử kết nối lại");
        try {
            openConnection();
        } catch (Exception e) {
            System.err.println("Lỗi khi thử kết nối lại: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean isConnected(){
        return socket != null ? socket.isConnected() : false;
    }
}
