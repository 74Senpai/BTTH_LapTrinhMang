package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.NhanVienDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.NhanVienService;

public class NhanVienController {

    private static final Gson gson = new Gson();
    private static final NhanVienService nhanVienService = new NhanVienService(); 

    @SuppressWarnings("UseSpecificCatch")
    public static String login(BaseDTO.Request req) {
        try {
            if (req.action().equals("LOGIN")) {
                NhanVienDTO.Login loginInfor = gson.fromJson(req.data(), NhanVienDTO.Login.class);
                NhanVienDTO.LoginStatus status = nhanVienService.checkLogin(loginInfor);
                if (status.isLogin()) {
                    AuthController.registerSession(status.session(), status);
                    System.out.println("User " + loginInfor.username() + " đăng nhập thành công!");
                }
                return DataBuilder.successRes(req, status);
            }
            return DataBuilder.notFoundRes(req);
        } catch (Exception e) {
            return DataBuilder.serverErrorRes(req, e.getMessage());
        }
    }
}
