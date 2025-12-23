package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.DTOs.ThongKeDTO;

public class ThongKeDAO {

    /**
     * Báo cáo tổng thu theo từng tháng trong năm
     */
    public List<ThongKeDTO.DoanhThuThang> getDoanhThuTheoThang(Connection conn, int nam) throws SQLException {
        String sql = """
            SELECT MONTH(NgayThanhToan) as Thang, YEAR(NgayThanhToan) as Nam, SUM(TongTien) as TongThu 
            FROM HoaDon 
            WHERE YEAR(NgayThanhToan) = ? 
            GROUP BY MONTH(NgayThanhToan), YEAR(NgayThanhToan)
            ORDER BY Thang ASC
        """;
        List<ThongKeDTO.DoanhThuThang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ThongKeDTO.DoanhThuThang(
                            rs.getInt("Thang"),
                            rs.getInt("Nam"),
                            rs.getDouble("TongThu")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Thống kê số lượng phòng theo trạng thái (Trống, Đang sử dụng, Đã đặt, Bảo
     * trì)
     */
    public List<ThongKeDTO.TrangThaiPhong> getThongKeTrangThaiPhong(Connection conn) throws SQLException {
        String sql = """
            SELECT t.TenTrangThai, COUNT(p.MaPhong) as SoLuong 
            FROM TrangThaiPhong t 
            LEFT JOIN Phong p ON t.MaTrangThai = p.MaTrangThai 
            WHERE t.MaTrangThai <> 6 -- Loại bỏ trạng thái 'Đã xóa'
            GROUP BY t.MaTrangThai, t.TenTrangThai
        """;
        List<ThongKeDTO.TrangThaiPhong> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ThongKeDTO.TrangThaiPhong(
                        rs.getString("TenTrangThai"),
                        rs.getInt("SoLuong")
                ));
            }
        }
        return list;
    }

    /**
     * Đếm tổng số khách hàng đang thuê (Dựa trên hợp đồng Active)
     */
    public int getTongKhachDangThue(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT MaKhachHang) FROM HopDongThue WHERE TrangThaiHopDong = 'Active'";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Lấy doanh thu của tháng hiện tại
     */
    public double getDoanhThuThangNay(Connection conn) throws SQLException {
        String sql = "SELECT SUM(TongTien) FROM HoaDon WHERE MONTH(NgayThanhToan) = MONTH(CURRENT_DATE()) AND YEAR(NgayThanhToan) = YEAR(CURRENT_DATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
}
