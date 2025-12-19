package homestay.Server.DAO;

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

import homestay.Server.Models.HopDongThue;
import homestay.Server.Models.KhachHang;
import homestay.Server.Models.Phong;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseManager {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL =
            "jdbc:mysql://" + dotenv.get("DB_HOST") + ":" +
                    dotenv.get("DB_PORT") + "/" +
                    dotenv.get("DB_NAME") +
                    "?useSSL=false&serverTimezone=UTC";

    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    // ===================== CONNECTION =====================
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ===================== TEST CONNECT =====================
    public static void main(String[] args) {
        try (Connection conn = new DatabaseManager().getConnection()) {
            System.out.println("✅ KẾT NỐI MYSQL THÀNH CÔNG");

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

            while (rs.next()) {
                String table = rs.getString("TABLE_NAME");
                Statement st = conn.createStatement();
                ResultSet cnt = st.executeQuery("SELECT COUNT(*) FROM " + table);
                cnt.next();
                System.out.println("→ " + table + " (" + cnt.getInt(1) + " dòng)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // ===================== PHÒNG ==========================
    // ======================================================

    // Lấy phòng trống
    public List<Phong> getPhongTrong() {
        List<Phong> list = new ArrayList<>();

        String sql = """
                SELECT p.MaPhong, p.TenPhong, p.GiaThueNgay, p.GiaThueThang,
                       p.SoDienHienTai, p.SoNuocHienTai,
                       t.MaTrangThai, t.TenTrangThai
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
                p.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                p.setGiaThueThang(rs.getDouble("GiaThueThang"));
                p.setSoDienHienTai(rs.getInt("SoDienHienTai"));
                p.setSoNuocHienTai(rs.getInt("SoNuocHienTai"));
                p.setMaTrangThai(rs.getInt("MaTrangThai"));
                p.setTenTrangThai(rs.getString("TenTrangThai"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm phòng
    public boolean themPhong(Phong p) {
        String sql = """
                INSERT INTO Phong
                (MaPhong, TenPhong, GiaThueNgay, GiaThueThang, MaTrangThai)
                VALUES (?, ?, ?, ?, 1)
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

    // ======================================================
    // ===================== KHÁCH HÀNG =====================
    // ======================================================

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setCccd(rs.getString("CCCD"));
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = """
                INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, CCCD)
                VALUES (?, ?, ?, ?)
                """;

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

    // ======================================================
    // ===================== HỢP ĐỒNG =======================
    // ======================================================

    public boolean taoHopDong(HopDongThue hd) {
        String insertHD = """
                INSERT INTO HopDongThue
                (MaHopDong, MaKhachHang, MaPhong, MaNhanVien,
                 LoaiHinhThue, NgayBatDau, NgayKetThuc, TrangThaiHopDong)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'Active')
                """;

        String updatePhong = "UPDATE Phong SET MaTrangThai = 2 WHERE MaPhong = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psHD = conn.prepareStatement(insertHD);
                 PreparedStatement psPhong = conn.prepareStatement(updatePhong)) {

                psHD.setString(1, hd.getMaHopDong());
                psHD.setString(2, hd.getMaKhachHang());
                psHD.setString(3, hd.getMaPhong());
                psHD.setObject(4, hd.getMaNhanVien());
                psHD.setString(5, hd.getLoaiHinhThue());
                psHD.setDate(6, Date.valueOf(hd.getNgayBatDau()));
                psHD.setDate(7, Date.valueOf(hd.getNgayKetThuc()));

                psHD.executeUpdate();

                psPhong.setString(1, hd.getMaPhong());
                psPhong.executeUpdate();

                conn.commit();
                return true;

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

    // ======================================================
    // ===================== HÓA ĐƠN ========================
    // ======================================================

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
            rs.next();
            return rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ======================================================
    // ===================== KHÁCH ĐANG THUÊ =================
    // ======================================================

    public List<Object[]> getKhachDangThue() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
                SELECT kh.HoTen, p.TenPhong,
                       h.NgayBatDau, h.NgayKetThuc,
                       h.TrangThaiHopDong
                FROM HopDongThue h
                JOIN KhachHang kh ON h.MaKhachHang = kh.MaKH
                JOIN Phong p ON h.MaPhong = p.MaPhong
                WHERE h.TrangThaiHopDong = 'Active'
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
