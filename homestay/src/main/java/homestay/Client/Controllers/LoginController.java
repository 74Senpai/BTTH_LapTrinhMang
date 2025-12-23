package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.Client.Helper.SessionManager;
import homestay.DTOs.BaseDTO;
import homestay.DTOs.NhanVienDTO;

public class LoginController {

    public LoginController() {}

    /**
     * Xử lý đăng nhập
     *
     * @param username
     * @param password
     * @return NhanVien nếu thành công, null nếu thất bại
     */
    public boolean login(String username, String password) {
        boolean isLogin = false;
        final String action = "LOGIN";
        ClientSocketController.ensureConnected();

        NhanVienDTO.Login data = new NhanVienDTO.Login(username, password);

        BaseDTO.Response response
                = ClientSocketController.sendRequest("AUTH", action, data, true);
        if (response.action().equals(action)) {
            NhanVienDTO.LoginStatus res = new Gson().fromJson(response.data(), NhanVienDTO.LoginStatus.class);
            if (res.maNV() != -1 && res.hoTen() != null) {
                SessionManager.setSession(res.session());
                isLogin = true;
            }
            System.out.println("User login: " + res.hoTen());
        }
        return isLogin;
    }
}
