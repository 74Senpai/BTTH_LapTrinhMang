package homestay.Server.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/Homestay?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "long";
    private static final String PASS = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // === TEST KẾT NỐI ===
    public static void main(String[] args) {
        try (Connection c = new DatabaseManager().getConnection()) {
            System.out.println("KẾT NỐI MYSQL THÀNH CÔNG!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === PHÒNG ===
    public List<Phong> getPhongTrong() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong WHERE TrangThai = 'Trống'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Phong p = new Phong();
                p.setMaPhong(rs.getString("MaPhong"));
                p.setTenPhong(rs.getString("TenPhong"));
                p.setTrangThai(rs.getString("TrangThai"));
                p.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                p.setGiaThueThang(rs.getDouble("GiaThueThang"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // === KHÁCH HÀNG ===
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, CCCD) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getCccd());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === HỢP ĐỒNG ===
    public boolean taoHopDong(HopDongThue hd) {
        String sql = "INSERT INTO HopDongThue (MaHopDong, MaKhachHang, MaPhong, MaNhanVien, LoaiHinhThue, NgayBatDau, NgayKetThuc) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHopDong());
            ps.setString(2, hd.getMaKhachHang());
            ps.setString(3, hd.getMaPhong());
            ps.setObject(4, hd.getMaNhanVien());
            ps.setString(5, hd.getLoaiHinhThue());
            ps.setDate(6, hd.getNgayBatDau() == null ? null : Date.valueOf(hd.getNgayBatDau()));
            ps.setDate(7, hd.getNgayKetThuc() == null ? null : Date.valueOf(hd.getNgayKetThuc()));
            int row = ps.executeUpdate();
            if (row > 0) {
                capNhatTrangThaiPhong(hd.getMaPhong(), "Đang thuê");
            }
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void capNhatTrangThaiPhong(String maPhong, String trangThai) {
        String sql = "UPDATE Phong SET TrangThai = ? WHERE MaPhong = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maPhong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === DOANH THU THEO THÁNG ===
    public double getDoanhThuThang(int thang, int nam) {
        String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HoaDon hd " +
                     "JOIN HopDongThue hdt ON hd.MaHopDong = hdt.MaHopDong " +
                     "WHERE MONTH(hdt.NgayBatDau) = ? AND YEAR(hdt.NgayBatDau) = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // === KHÁCH ĐANG THUÊ (sửa GETDATE() → CURDATE()) ===
    public List<Object[]> getKhachDangThue() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT kh.HoTen, p.TenPhong, hdt.NgayBatDau, hdt.NgayKetThuc " +
                     "FROM HopDongThue hdt " +
                     "JOIN KhachHang kh ON hdt.MaKhachHang = kh.MaKH " +
                     "JOIN Phong p ON hdt.MaPhong = p.MaPhong " +
                     "WHERE hdt.NgayKetThuc IS NULL OR hdt.NgayKetThuc >= CURDATE()";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getDate(4)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}