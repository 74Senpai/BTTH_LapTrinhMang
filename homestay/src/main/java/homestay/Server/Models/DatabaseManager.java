package homestay.Server.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Homestays;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "long";
    private static final String PASS = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void main(String[] args) {
        try (Connection c = new DatabaseManager().getConnection()) {
            System.out.println("KẾT NỐI SQL SERVER (Homestays) THÀNH CÔNG!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Phong> getPhongTrong() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT p.*, t.TenTrangThai FROM Phong p JOIN TrangThaiPhong t ON p.MaTrangThai = t.MaTrangThai WHERE p.MaTrangThai = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Phong p = new Phong();
                p.setMaPhong(rs.getString("MaPhong"));
                p.setTenPhong(rs.getString("TenPhong"));
                p.setMaTrangThai(rs.getInt("MaTrangThai"));
                p.setTenTrangThai(rs.getString("TenTrangThai"));
                p.setSoDienHienTai(rs.getInt("SoDienHienTai"));
                p.setSoNuocHienTai(rs.getInt("SoNuocHienTai"));
                p.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                p.setGiaThueThang(rs.getDouble("GiaThueThang"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themPhong(Phong p) {
        String sql = "INSERT INTO Phong (MaPhong, TenPhong, GiaThueNgay, GiaThueThang, MaTrangThai, SoDienHienTai, SoNuocHienTai) VALUES (?, ?, ?, ?, 1, 0, 0)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTenPhong());
            ps.setDouble(3, p.getGiaThueNgay());
            ps.setDouble(4, p.getGiaThueThang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public boolean taoHopDong(HopDongThue hd) {
        String sqlInsert = "INSERT INTO HopDongThue (MaHopDong, MaKhachHang, MaPhong, MaNhanVien, LoaiHinhThue, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE Phong SET MaTrangThai = 2 WHERE MaPhong = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlInsert);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {

                ps1.setString(1, hd.getMaHopDong());
                ps1.setString(2, hd.getMaKhachHang());
                ps1.setString(3, hd.getMaPhong());
                ps1.setObject(4, hd.getMaNhanVien());
                ps1.setString(5, hd.getLoaiHinhThue());
                ps1.setDate(6, hd.getNgayBatDau() == null ? null : Date.valueOf(hd.getNgayBatDau()));
                ps1.setDate(7, hd.getNgayKetThuc() == null ? null : Date.valueOf(hd.getNgayKetThuc()));

                int row = ps1.executeUpdate();
                if (row > 0) {
                    ps2.setString(1, hd.getMaPhong());
                    ps2.executeUpdate();
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getDoanhThuThang(int thang, int nam) {
        String sql = "SELECT ISNULL(SUM(TongTien), 0) FROM HoaDon WHERE MONTH(NgayThanhToan) = ? AND YEAR(NgayThanhToan) = ?";
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

    public List<Object[]> getKhachDangThue() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT kh.HoTen, p.TenPhong, hdt.NgayBatDau, hdt.NgayKetThuc, t.TenTrangThai
            FROM HopDongThue hdt
            JOIN KhachHang kh ON hdt.MaKhachHang = kh.MaKH
            JOIN Phong p ON hdt.MaPhong = p.MaPhong
            JOIN TrangThaiPhong t ON p.MaTrangThai = t.MaTrangThai
            WHERE hdt.NgayKetThuc IS NULL OR hdt.NgayKetThuc >= GETDATE()
            """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString(1), rs.getString(2),
                    rs.getDate(3), rs.getDate(4),
                    rs.getString(5)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}