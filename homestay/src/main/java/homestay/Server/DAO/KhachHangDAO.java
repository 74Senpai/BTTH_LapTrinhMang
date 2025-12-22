package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.KhachHang;

public class KhachHangDAO {

    public boolean insert(Connection conn, KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (HoTen, SoDienThoai, CCCD) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getCccd());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Connection conn, KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET HoTen = ?, SoDienThoai = ?, CCCD = ? WHERE MaKH = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getCccd());
            ps.setInt(4, kh.getMaKH());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(Connection conn, int maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;
        }
    }

    public KhachHang findById(Connection conn, int maKH) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maKH);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        }
        return null;
    }

    public KhachHang findByCCCD(Connection conn, String cccd) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE CCCD = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cccd);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        }
        return null;
    }

    public List<KhachHang> getAll(Connection conn) throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }
        }
        return list;
    }

    // ===================== MAP RESULTSET =====================
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getInt("MaKH"));
        kh.setHoTen(rs.getString("HoTen"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setCccd(rs.getString("CCCD"));
        return kh;
    }
}
