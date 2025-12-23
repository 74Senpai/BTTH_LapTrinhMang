package homestay.Client.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import homestay.Client.Helper.SessionManager;
import homestay.DTOs.BaseDTO;
import homestay.DTOs.NhanVienDTO;

public class LoginController {

    public LoginController() {
    }

    /**
     * Xử lý đăng nhập
     *
     * @param username
     * @param password
     * @return NhanVien nếu thành công, null nếu thất bại
     */
    public boolean login(String username, String password) throws Exception {

        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Tên đăng nhập và mật khẩu không được để trống!");
        }
        boolean isLogin = false;
        final String action = "LOGIN";
        ClientSocketController.ensureConnected();

        NhanVienDTO.Login data = new NhanVienDTO.Login(username, password);
        try {
            BaseDTO.Response response
                    = ClientSocketController.sendRequest("AUTH", action, data, true);
            if(response == null){
                throw new Exception("Không nhận được phản hồi từ server.");
            }
            if (response.action().equals(action)) {
                NhanVienDTO.LoginStatus res = new Gson().fromJson(response.data(), NhanVienDTO.LoginStatus.class);
                if (res.maNV() != -1 && res.hoTen() != null && res.isLogin()) {
                    SessionManager.setSession(res.session());
                    isLogin = true;
                }else{
                    throw new Exception(res.message());
                }
                System.out.println("User login: " + res.hoTen());
            }
            return isLogin;
        } catch (JsonSyntaxException e) {
            System.err.println(e.getMessage());
            throw new JsonSyntaxException(" Lỗi thao tác với dữ liệu! Đăng nhập thất bại");
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
            throw new Exception("Server không phản hồi: "+e.getMessage());
        } 

    }
}
