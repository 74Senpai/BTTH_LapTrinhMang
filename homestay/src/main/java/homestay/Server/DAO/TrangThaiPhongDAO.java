package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.TrangThaiPhong;

public class TrangThaiPhongDAO {

    public List<TrangThaiPhong> getAllTrangThai() throws SQLException {
        List<TrangThaiPhong> lstTrangThai = new ArrayList<>();
        String query = "SELECT * FROM TrangThaiPhong WHERE MaTrangThai <> ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, PhongDAO.TRANG_THAI_DA_XOA);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TrangThaiPhong ttp = new TrangThaiPhong();
                ttp.setMaTrangThai(rs.getInt("MaTrangThai"));
                ttp.setTenTrangThai(rs.getString("TenTrangThai"));
                lstTrangThai.add(ttp);
            }
        }

        return lstTrangThai;
    }

    /**
     * Lấy tên trạng thái theo mã
     */
    public String getTenTrangThaiByMa(int maTrangThai) throws SQLException {
        String query = "SELECT TenTrangThai FROM TrangThaiPhong WHERE MaTrangThai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, maTrangThai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenTrangThai");
                } else {
                    return null;
                }
            }
        }
    }
}
