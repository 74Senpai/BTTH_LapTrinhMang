package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import homestay.Server.Models.NhanVien;

public class NhanVienDAO {

    public NhanVien findByUserName(String userName) throws SQLException {
        String query = "SELECT * FROM NhanVien WHERE Username = ?";
        System.out.println("Find user: "+userName);
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, userName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getInt("MaNV"));
                    nv.setHoTen(rs.getString("HoTen"));
                    nv.setUsername(rs.getString("Username"));
                    nv.setPassword(rs.getString("Password"));
                    return nv;
                } else {
                    System.out.println("Không có dữ liệu trả về");
                    return null;
                }
            }
        }
    }
}
