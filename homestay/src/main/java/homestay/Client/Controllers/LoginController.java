package homestay.Client.Controllers;

import homestay.Client.DTOs.NhanVienDTO;

public class LoginController {

    private final ClientSocketController clientSocketController;

    public LoginController() {
        this.clientSocketController = new ClientSocketController("localhost", 8000);
    }

    /**
     * Xử lý đăng nhập
     * @param username
     * @param password
     * @return NhanVien nếu thành công, null nếu thất bại
     */
    public NhanVienDTO.NhanVienViewDTO login(String username, String password) {

        NhanVienDTO.NhanVienLoginDTO data = new NhanVienDTO.NhanVienLoginDTO(username, password);

        Object response = this.clientSocketController.sendRequest("LOGIN", data, true);

        if (response instanceof NhanVienDTO.NhanVienViewDTO) {
            return (NhanVienDTO.NhanVienViewDTO) response;
        }

        return null;
    }
}
