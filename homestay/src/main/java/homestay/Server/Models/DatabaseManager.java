package homestay.Server.Models;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseManager {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = "jdbc:mysql://" +
            dotenv.get("DB_HOST") + ":" +
            dotenv.get("DB_PORT") + "/" +
            dotenv.get("DB_NAME") +
            "?useSSL=false&serverTimezone=UTC";

    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ===================== MAIN TEST – LOG BẢNG =========================
    public static void main(String[] args) {
        try (Connection c = new DatabaseManager().getConnection()) {

            System.out.println("KẾT NỐI MYSQL (Homestays) THÀNH CÔNG!!!");
            System.out.println("==== DANH SÁCH BẢNG ====");

            DatabaseMetaData meta = c.getMetaData();
            String dbName = c.getCatalog();

            ResultSet tables = meta.getTables(dbName, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                System.out.print("->" + tableName);

                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM `" + tableName + "`")) {

                    if (rs.next()) {
                        System.out.println("  (Số dòng: " + rs.getInt(1) + ")");
                    }
                } catch (Exception ex) {
                    System.out.println("  (Không thể đếm dòng)");
                }
            }

            System.out.println("==================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== LẤY PHÒNG TRỐNG ====================
    public List<Phong> getPhongTrong() {
        List<Phong> list = new ArrayList<>();
        String sql = """
                SELECT p.*, t.TenTrangThai 
                FROM Phong p 
                JOIN TrangThaiPhong t ON p.MaTrangThai = t.MaTrangThai 
                WHERE p.MaTrangThai = 1
                """;

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

    // ==================== THÊM PHÒNG ====================
    public boolean themPhong(Phong p) {
        String sql = """
                INSERT INTO Phong (MaPhong, TenPhong, GiaThueNgay, GiaThueThang, MaTrangThai, SoDienHienTai, SoNuocHienTai)
                VALUES (?, ?, ?, ?, 1, 0, 0)
                """;

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

    // ==================== THÊM KHÁCH HÀNG ====================
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

    // ==================== TẠO HỢP ĐỒNG ====================
    public boolean taoHopDong(HopDongThue hd) {

        String sqlInsert = """
                INSERT INTO HopDongThue 
                (MaHopDong, MaKhachHang, MaPhong, MaNhanVien, LoaiHinhThue, NgayBatDau, NgayKetThuc, TrangThaiHopDong)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'Active')
                """;

        String sqlUpdatePhong = "UPDATE Phong SET MaTrangThai = 2 WHERE MaPhong = ?";

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlInsert);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUpdatePhong)) {

                ps1.setString(1, hd.getMaHopDong());
                ps1.setString(2, hd.getMaKhachHang());
                ps1.setString(3, hd.getMaPhong());
                ps1.setObject(4, hd.getMaNhanVien());
                ps1.setString(5, hd.getLoaiHinhThue());
                ps1.setDate(6, Date.valueOf(hd.getNgayBatDau()));
                ps1.setDate(7, Date.valueOf(hd.getNgayKetThuc()));

                int row = ps1.executeUpdate();

                if (row > 0) {
                    ps2.setString(1, hd.getMaPhong());
                    ps2.executeUpdate();
                    conn.commit();
                    return true;
                }

                conn.rollback();
                return false;

            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== CẬP NHẬT TRẠNG THÁI PHÒNG ====================
    public void capNhatTrangThaiPhong(String maPhong, int maTrangThai) {
        String sql = "UPDATE Phong SET MaTrangThai = ? WHERE MaPhong = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maTrangThai);
            ps.setString(2, maPhong);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== DOANH THU THEO THÁNG (MYSQL) ====================
    public double getDoanhThuThang(int thang, int nam) {
        String sql = """
                SELECT COALESCE(SUM(TongTien), 0)
                FROM HoaDon 
                WHERE MONTH(NgayThanhToan) = ? AND YEAR(NgayThanhToan) = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ==================== KHÁCH ĐANG THUÊ ====================
    public List<Object[]> getKhachDangThue() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
                SELECT kh.HoTen, p.TenPhong, hdt.NgayBatDau, hdt.NgayKetThuc, t.TenTrangThai
                FROM HopDongThue hdt
                JOIN KhachHang kh ON hdt.MaKhachHang = kh.MaKH
                JOIN Phong p ON hdt.MaPhong = p.MaPhong
                JOIN TrangThaiPhong t ON p.MaTrangThai = t.MaTrangThai
                WHERE hdt.NgayKetThuc >= CURDATE()
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getString(5)
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
