package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.HoaDon;

public class HoaDonDAO {

    /**
     * TÍNH TOÁN TIỀN PHÒNG TỰ ĐỘNG: Lấy đơn giá từ bảng Phong và tính dựa trên
     * loại hình thuê/thời gian thuê trong HopDongThue
     */
    public double calculateTienPhong(Connection conn, int maHopDong) throws SQLException {
        String sql = """
        SELECT 
            hd.LoaiHinhThue, 
            hd.NgayBatDau, 
            hd.NgayKetThuc, 
            hd.TrangThaiHopDong, 
            p.GiaThueNgay, 
            p.GiaThueThang
        FROM HopDongThue hd
        JOIN Phong p ON hd.MaPhong = p.MaPhong
        WHERE hd.MaHopDong = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHopDong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // KIỂM TRA LOGIC HỦY Ở ĐÂY
                    String trangThai = rs.getString("TrangThaiHopDong");
                    if ("Cancelled".equals(trangThai)) {
                        throw new SQLException("Hợp đồng này đã bị hủy, không thể tạo hóa đơn.");
                    }

                    String loaiHinh = rs.getString("LoaiHinhThue");
                    Date start = rs.getDate("NgayBatDau");
                    Date end = rs.getDate("NgayKetThuc");

                    if ("Ngày".equals(loaiHinh)) {
                        double giaNgay = rs.getDouble("GiaThueNgay");
                        long diffInMillies = Math.abs(end.getTime() - start.getTime());
                        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
                        if (diffInDays == 0) {
                            diffInDays = 1;
                        }
                        return diffInDays * giaNgay;
                    } else {
                        return rs.getDouble("GiaThueThang");
                    }
                } else {
                    throw new SQLException("Không tìm thấy thông tin hợp đồng.");
                }
            }
        }
    }

    /**
     * TÍNH TIỀN ĐIỆN NƯỚC: Lấy số tiêu thụ từ bảng DienNuocHangThang và nhân
     * với đơn giá truyền từ Service
     */
    public double getTienDienNuocTuChiSo(Connection conn, int maDienNuoc, double giaDien, double giaNuoc) throws SQLException {
        String sql = "SELECT SoDienTieuThu, SoNuocTieuThu FROM DienNuocHangThang WHERE MaDienNuoc = ?";
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

    /**
     * HỦY HỢP ĐỒNG VÀ GIẢI PHÓNG PHÒNG (MySQL UPDATE JOIN): Cập nhật đồng thời
     * trạng thái hợp đồng và đưa phòng về trạng thái 'Trống' (1)
     */
    public void cancelHopDongVaGiaiPhongPhong(Connection conn, int maHopDong) throws SQLException {
        String sql = """
            UPDATE HopDongThue hd
            JOIN Phong p ON hd.MaPhong = p.MaPhong
            SET hd.TrangThaiHopDong = 'Cancelled',
                p.MaTrangThai = 1
            WHERE hd.MaHopDong = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHopDong);
            ps.executeUpdate();
        }
    }

    /**
     * THÊM MỚI HÓA ĐƠN
     */
    public int insertHoaDon(Connection conn, HoaDon hd) throws SQLException {
        String sql = """
            INSERT INTO HoaDon (MaHopDong, MaDienNuoc, TienPhong, TienChiPhiPhu, TongTien, TrangThaiThanhToan)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hd.getMaHopDong());
            if (hd.getMaDienNuoc() != null) {
                ps.setInt(2, hd.getMaDienNuoc());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setDouble(3, hd.getTienPhong());
            ps.setDouble(4, hd.getTienChiPhiPhu());
            ps.setDouble(5, hd.getTongTien());
            ps.setInt(6, hd.getTrangThaiThanhToan());

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
     * CẬP NHẬT HÓA ĐƠN
     */
    public boolean updateHoaDon(Connection conn, HoaDon hd) throws SQLException {
        String sql = """
        UPDATE HoaDon 
        SET TienChiPhiPhu = ?, TongTien = ?, TrangThaiThanhToan = ? 
        WHERE MaThanhToan = ? AND TrangThaiThanhToan = 0
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, hd.getTienChiPhiPhu());
            ps.setDouble(2, hd.getTongTien());
            ps.setInt(3, hd.getTrangThaiThanhToan());
            ps.setInt(4, hd.getMaThanhToan());

            int rowsAffected = ps.executeUpdate();

            // Trả về true nếu cập nhật thành công (tức là trạng thái cũ đang là 0)
            // Trả về false nếu không tìm thấy hóa đơn hoặc hóa đơn đã thanh toán (trạng thái != 0)
            return rowsAffected > 0;
        }
    }

    /**
     * XÓA HÓA ĐƠN
     */
    public boolean deleteHoaDon(Connection conn, int maThanhToan) throws SQLException {
        // Chỉ xóa nếu mã thanh toán khớp VÀ trạng thái thanh toán là 0 (Chưa thanh toán)
        String sql = "DELETE FROM HoaDon WHERE MaThanhToan = ? AND TrangThaiThanhToan = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maThanhToan);

            int rowsDeleted = ps.executeUpdate();

            // Trả về true nếu xóa thành công
            // Trả về false nếu hóa đơn không tồn tại hoặc đã thanh toán (nên không bị xóa)
            return rowsDeleted > 0;
        }
    }

    /**
     * LẤY CHI TIẾT HÓA ĐƠN THEO ID (Kèm thông tin JOIN)
     */
    public HoaDon getHoaDonById(Connection conn, int maThanhToan) throws SQLException {
        String query = """
            SELECT h.*, kh.HoTen AS TenKhachHang, p.TenPhong
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
     * LẤY TẤT CẢ HÓA ĐƠN (Kèm thông tin JOIN)
     */
    public List<HoaDon> getAllHoaDon(Connection conn) throws SQLException {
        String query = """
            SELECT h.*, kh.HoTen AS TenKhachHang, p.TenPhong
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
     * Helper mapping dữ liệu từ ResultSet sang Model
     */
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaThanhToan(rs.getInt("MaThanhToan"));
        hd.setMaHopDong(rs.getInt("MaHopDong"));

        int maDN = rs.getInt("MaDienNuoc");
        hd.setMaDienNuoc(rs.wasNull() ? null : maDN);

        hd.setTienPhong(rs.getDouble("TienPhong"));
        hd.setTienChiPhiPhu(rs.getDouble("TienChiPhiPhu"));
        hd.setTongTien(rs.getDouble("TongTien"));
        hd.setTrangThaiThanhToan(rs.getInt("TrangThaiThanhToan"));

        Timestamp ts = rs.getTimestamp("NgayThanhToan");
        if (ts != null) {
            hd.setNgayThanhToan(ts.toLocalDateTime());
        }

        hd.setTenKhachHang(rs.getString("TenKhachHang"));
        hd.setTenPhong(rs.getString("TenPhong"));
        return hd;
    }
}
