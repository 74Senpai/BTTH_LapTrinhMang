package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.HoaDon;

public class HoaDonDAO {

    /**
     * Lấy tất cả hóa đơn kèm thông tin JOIN
     */
    public List<HoaDon> getAllHoaDon(Connection conn) throws SQLException {
        String query = """
            SELECT 
                h.MaThanhToan, h.MaHopDong, h.MaDienNuoc,
                h.TienPhong, h.TienChiPhiPhu, h.TongTien, h.NgayThanhToan,
                kh.HoTen AS TenKhachHang, p.TenPhong
            FROM HoaDon h
            JOIN HopDongThue hd ON h.MaHopDong = hd.MaHopDong
            JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKH
            JOIN Phong p ON hd.MaPhong = p.MaPhong
            ORDER BY h.NgayThanhToan DESC
        """;

        List<HoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToHoaDon(rs));
            }
        }
        return list;
    }

    /**
     * Lấy hóa đơn theo ID kèm thông tin JOIN
     */
    public HoaDon getHoaDonById(Connection conn, int maThanhToan) throws SQLException {
        String query = """
            SELECT 
                h.MaThanhToan, h.MaHopDong, h.MaDienNuoc,
                h.TienPhong, h.TienChiPhiPhu, h.TongTien, h.NgayThanhToan,
                kh.HoTen AS TenKhachHang, p.TenPhong
            FROM HoaDon h
            JOIN HopDongThue hd ON h.MaHopDong = hd.MaHopDong
            JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKH
            JOIN Phong p ON hd.MaPhong = p.MaPhong
            WHERE h.MaThanhToan = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maThanhToan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHoaDon(rs);
                }
            }
        }
        return null;
    }

    /**
     * Thêm mới hóa đơn
     */
    public int insertHoaDon(Connection conn, HoaDon hd) throws SQLException {
        String sql = """
            INSERT INTO HoaDon (MaHopDong, MaDienNuoc, TienPhong, TienChiPhiPhu, TongTien, NgayThanhToan)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, Integer.parseInt(hd.getMaHopDong())); // Ép kiểu nếu Model vẫn để String
            
            if (hd.getMaDienNuoc() != null) {
                ps.setInt(2, hd.getMaDienNuoc());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            
            ps.setDouble(3, hd.getTienPhong());
            ps.setDouble(4, hd.getTienChiPhiPhu());
            ps.setDouble(5, hd.getTongTien());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Cập nhật hóa đơn
     */
    public boolean updateHoaDon(Connection conn, HoaDon hd) throws SQLException {
        String sql = """
            UPDATE HoaDon 
            SET MaDienNuoc = ?, TienPhong = ?, TienChiPhiPhu = ?, TongTien = ? 
            WHERE MaThanhToan = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (hd.getMaDienNuoc() != null) {
                ps.setInt(1, hd.getMaDienNuoc());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setDouble(2, hd.getTienPhong());
            ps.setDouble(3, hd.getTienChiPhiPhu());
            ps.setDouble(4, hd.getTongTien());
            ps.setInt(5, Integer.parseInt(hd.getMaThanhToan()));

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa hóa đơn
     */
    public boolean deleteHoaDon(Connection conn, int maThanhToan) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE MaThanhToan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maThanhToan);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper mapping để dùng chung cho các hàm SELECT
     */
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        // Set ID về String để khớp với Model của bạn (nếu Model dùng String)
        hd.setMaThanhToan(String.valueOf(rs.getInt("MaThanhToan")));
        hd.setMaHopDong(String.valueOf(rs.getInt("MaHopDong")));
        
        int maDN = rs.getInt("MaDienNuoc");
        hd.setMaDienNuoc(rs.wasNull() ? null : maDN);
        
        hd.setTienPhong(rs.getDouble("TienPhong"));
        hd.setTienChiPhiPhu(rs.getDouble("TienChiPhiPhu"));
        hd.setTongTien(rs.getDouble("TongTien"));
        
        // Map LocalDateTime
        if (rs.getTimestamp("NgayThanhToan") != null) {
            hd.setNgayThanhToan(rs.getTimestamp("NgayThanhToan").toLocalDateTime());
        }
        
        // Gán các trường Join
        hd.setTenKhachHang(rs.getString("TenKhachHang"));
        hd.setTenPhong(rs.getString("TenPhong"));
        
        return hd;
    }
}