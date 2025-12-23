package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;

import homestay.DTOs.NhanVienDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.NhanVienDAO;
import homestay.Server.Models.NhanVien;

public class NhanVienService {

    private final NhanVienDAO dao = new NhanVienDAO();

    public NhanVienDTO.LoginStatus checkLogin(NhanVienDTO.Login login) {

        try (Connection conn = DBConnection.getConnection()) {

            NhanVien nv = dao.findByUserName(conn, login.username());

            if (nv == null) {
                return new NhanVienDTO.LoginStatus(
                        -1,
                        null,
                        false,
                        "User do not exists",
                        null
                );
            }

            if (nv.getPassword().equals(login.password())) {
                return new NhanVienDTO.LoginStatus(
                        nv.getMaNV(),
                        nv.getHoTen(),
                        true,
                        "Login success",
                        nv.getUsername() + "_" + nv.getMaNV()
                );
            }

            return new NhanVienDTO.LoginStatus(
                    -1,
                    null,
                    false,
                    "Invalid password",
                    null
            );

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
