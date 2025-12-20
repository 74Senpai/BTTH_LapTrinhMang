package homestay.Server.Services;

import java.sql.SQLException;

import homestay.Server.DAO.NhanVienDAO;
import homestay.Server.DTOs.NhanVienDTO;
import homestay.Server.Models.NhanVien;

public class NhanVienService {

    private final NhanVienDAO dao = new NhanVienDAO();

    public NhanVienDTO.LoginStatus checkLogin(NhanVienDTO.Login login) {
        try {
            NhanVien nv = this.dao.findByUserName(login.getUsername());
            if (nv == null) {
                return new NhanVienDTO.LoginStatus(
                        -1,
                        null,
                        false,
                        "User do not exists",
                        null
                );
            }
            if (nv.getPassword().equals(login.getPassword())) {
                return new NhanVienDTO.LoginStatus(
                        nv.getMaNV(),
                        nv.getHoTen(),
                        true,
                        "Login success",
                        nv.getUsername() + "_" + nv.getMaNV()
                );
            } else {
                return new NhanVienDTO.LoginStatus(
                        -1,
                        null,
                        false,
                        "Invalid password",
                        null
                );
            }
        } catch (SQLException e) {
            System.err.println("Không thể truy vấn dữ liệu nhân viên: " + e.getMessage());
            return new NhanVienDTO.LoginStatus(
                    -1,
                    null,
                    false,
                    "Database error",
                    null
            );
        }
    }
}
