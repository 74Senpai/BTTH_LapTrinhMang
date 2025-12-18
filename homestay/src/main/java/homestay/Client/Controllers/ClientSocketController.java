package homestay.Client.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import com.google.gson.Gson;

public class ClientSocketController {

    private Socket socket;
    private final String domain;
    private final int port;
    private final Gson gson = new Gson();
    private DataInputStream inputStream = null;
    private final int SO_TIMEOUT = 10000;

    public ClientSocketController(String domain, int port) {
        this.domain = domain;
        this.port = port;
    }

    public void openConnection() throws Exception {
        try {
            this.socket = new Socket(this.domain, this.port);
            this.socket.setSoTimeout(this.SO_TIMEOUT);
        } catch (UnknownHostException e) {
            System.err.println("Địa chỉ hoặc cổng không hợp lệ: " + e.getMessage());
            throw new Exception("Địa chỉ hoặc cổng không hợp lệ: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối tới server: " + e.getMessage());
            throw new Exception("Lỗi khi kết nối tới server: " + e.getMessage());
        } finally {
            if (!this.socket.isConnected()) {
                System.err.println("Kết nối tới server thất bại!!!");
            } else {
                System.out.println("Kết nối tới server thành công!");
            }
        }
    }

    public void closeConnection() throws Exception {
        if (this.socket.isConnected()) {
            this.socket.close();
            System.out.println("Đã ngắt kết nối tới server");
        } else {
            System.out.println(" Không có kết nối nào tới server");
        }
    }

    public void sendMessage(String action, Object mess, boolean fallback) {
        if (!this.ensureConnected()) {
            System.err.println("Gửi message không thành công! \n Message:" + action + ":" + mess);
            return;
        }
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            String jsonMess = this.buildMessage(action, mess);
            outputStream.writeUTF(jsonMess);
            System.out.println("Gửi yêu cầu thành công!");
        } catch (IOException e) {
            System.err.println("Không thể gửi yêu cầu tới server: " + e.getMessage());
        } finally {
            if (fallback) {
                System.out.println("Tiến hành thử lại.");
                sendMessage(action, mess, false);
            }
        }
    }

    public void initListener() {
        if (!this.ensureConnected()) {
            System.err.println("Không thể khởi tạo Listener");
            return;
        }
        try {
            this.inputStream = new DataInputStream(this.socket.getInputStream());
            System.out.println("Khởi tạo listener thành công!");
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo Listener: " + e.getMessage());
        }
    }

    public String listenMessage() throws Exception {
        if (this.inputStream == null) {
            throw new IllegalStateException("Listener chưa được khởi tạo. Gọi initListener() trước.");
        }
        return this.inputStream.readUTF();
    }

    public Object sendRequest(String action, Object data, boolean fallback) {
        if (!ensureConnected()) {
            System.err.println("Không có kết nối tới server. Gửi request thất bại!");
            return null;
        }

        if (this.inputStream == null) {
            initListener();
        }

        try {
            this.sendMessage(action, data, fallback);
            String responseJson = this.listenMessage();
            return gson.fromJson(responseJson, Object.class);
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi request: " + e.getMessage());
            return null;
        }
    }

    public void closeListener() throws Exception {
        if (this.inputStream != null) {
            this.inputStream.close();
            System.out.println("Đóng listenner");
        }
    }

    public void kill() {
        try {
            this.closeListener();
            this.closeConnection();
            System.out.println("Đã dừng Socket");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String buildMessage(String action, Object data) {
        HashMap<String, Object> mess = new HashMap<>();
        mess.put("action", action);
        mess.put("data", data);

        return this.gson.toJson(mess);
    }

    private boolean ensureConnected() {
        if (!this.socket.isConnected()) {
            System.err.println("Không có kết nối tới server");
            System.out.println("Thử kết nối lại");
            try {
                this.openConnection();
            } catch (Exception e) {
                System.err.println("Lỗi khi thử kết nối lại: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
