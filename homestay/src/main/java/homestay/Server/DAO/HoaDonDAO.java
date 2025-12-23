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
                h.TrangThaiThanhToan,
                kh.HoTen AS TenKhachHang, p.TenPhong
            FROM HoaDon h
            JOIN HopDongThue hd ON h.MaHopDong = hd.MaHopDong
            JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKH
            JOIN Phong p ON hd.MaPhong = p.MaPhong
            ORDER BY h.NgayThanhToan DESC
        """;

        List<HoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
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
                h.TrangThaiThanhToan,
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
            INSERT INTO HoaDon (MaHopDong, MaDienNuoc, TienPhong, TienChiPhiPhu, TongTien, NgayThanhToan, TrangThaiThanhToan)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hd.getMaHopDong());

            if (hd.getMaDienNuoc() != null) {
                ps.setInt(2, hd.getMaDienNuoc());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setDouble(3, hd.getTienPhong());
            ps.setDouble(4, hd.getTienChiPhiPhu());
            ps.setDouble(5, hd.getTongTien());
            ps.setInt(6, hd.getTrangThaiThanhToan() != null ? hd.getTrangThaiThanhToan() : 0);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
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
            SET TienChiPhiPhu = ?, TongTien = ?, TrangThaiThanhToan = ? 
            WHERE MaThanhToan = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, hd.getTienChiPhiPhu());
            ps.setDouble(2, hd.getTongTien());
            ps.setInt(3, hd.getTrangThaiThanhToan() != null ? hd.getTrangThaiThanhToan() : 0);
            ps.setInt(4, hd.getMaThanhToan());

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
     * Helper mapping dữ liệu từ ResultSet sang Object Model
     */
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();

        // Gán các trường ID kiểu int
        hd.setMaThanhToan(rs.getInt("MaThanhToan"));
        hd.setMaHopDong(rs.getInt("MaHopDong"));

        // Xử lý MaDienNuoc có thể Null
        int maDN = rs.getInt("MaDienNuoc");
        hd.setMaDienNuoc(rs.wasNull() ? null : maDN);

        hd.setTienPhong(rs.getDouble("TienPhong"));
        hd.setTienChiPhiPhu(rs.getDouble("TienChiPhiPhu"));
        hd.setTongTien(rs.getDouble("TongTien"));

        // Gán trạng thái thanh toán
        hd.setTrangThaiThanhToan(rs.getInt("TrangThaiThanhToan"));

        // Map LocalDateTime từ SQL Timestamp
        java.sql.Timestamp ts = rs.getTimestamp("NgayThanhToan");
        if (ts != null) {
            hd.setNgayThanhToan(ts.toLocalDateTime());
        }

        // Gán các trường lấy từ câu lệnh JOIN
        hd.setTenKhachHang(rs.getString("TenKhachHang"));
        hd.setTenPhong(rs.getString("TenPhong"));

        return hd;
    }

    public double getGiaThueTuHopDong(Connection conn, int maHopDong) throws SQLException {
        String sql = "SELECT GiaThue FROM HopDongThue WHERE MaHopDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHopDong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("GiaThue");
            }
        }
        return 0;
    }

    /**
     * Truy vấn chỉ số điện nước và tính tiền dựa trên đơn giá truyền vào
     */
    public double getTienDienNuocTuChiSo(Connection conn, int maDienNuoc, double giaDien, double giaNuoc) throws SQLException {
        String sql = "SELECT SoDienTieuThu, SoNuocTieuThu FROM DienNuoc WHERE MaDienNuoc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDienNuoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double tienDien = rs.getInt("SoDienTieuThu") * giaDien;
                    double tienNuoc = rs.getInt("SoNuocTieuThu") * giaNuoc;
                    return tienDien + tienNuoc;
                }
            }
        }
        return 0;
    }
}
