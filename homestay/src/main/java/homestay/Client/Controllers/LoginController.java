package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.Client.DTOs.BaseDTO;
import homestay.Client.DTOs.NhanVienDTO;
import homestay.Client.Helper.SessionManager;

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

        NhanVienDTO.NhanVienLoginDTO data = new NhanVienDTO.NhanVienLoginDTO(username, password);

        BaseDTO.Response response
                = ClientSocketController.sendRequest("AUTH", action, data, true);
        if (response.getAction().equals(action)) {
            NhanVienDTO.LoginResult res = new Gson().fromJson(response.getData(), NhanVienDTO.LoginResult.class);
            if (res.getMaNV() != -1 && res.getHoTen() != null) {
                SessionManager.setSession(res.getSession());
                isLogin = true;
            }
            System.out.println("User login: " + res.getHoTen());
        }
        return isLogin;
    }
}
