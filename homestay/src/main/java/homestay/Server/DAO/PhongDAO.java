package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.Phong;

public class PhongDAO {

    public static final int TRANG_THAI_DA_XOA = 6;
    public static final int TRANG_THAI_TRONG = 1;
    public static final int TRANG_THAI_SU_DUNG = 2;

    public List<Phong> getAllPhong(Connection conn) throws SQLException {
        String query = """
        SELECT 
            p.MaPhong,
            p.TenPhong,
            p.MaTrangThai,
            t.TenTrangThai,
            p.SoDienHienTai,
            p.SoNuocHienTai,
            p.GiaThueNgay,
            p.GiaThueThang
        FROM Phong p
        INNER JOIN TrangThaiPhong t
            ON p.MaTrangThai = t.MaTrangThai
        WHERE p.MaTrangThai <> ?
    """;

        List<Phong> lstPhong = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, TRANG_THAI_DA_XOA);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Phong p = new Phong();
                    p.setMaPhong(rs.getInt("MaPhong"));
                    p.setTenPhong(rs.getString("TenPhong"));
                    p.setMaTrangThai(rs.getInt("MaTrangThai"));
                    p.setTenTrangThai(rs.getString("TenTrangThai"));
                    p.setSoDienHienTai(rs.getInt("SoDienHienTai"));
                    p.setSoNuocHienTai(rs.getInt("SoNuocHienTai"));
                    p.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                    p.setGiaThueThang(rs.getDouble("GiaThueThang"));

                    lstPhong.add(p);
                }
            }
        }
        return lstPhong;
    }

    public List<Phong> getPhongTrong(Connection conn) throws SQLException {
        String query = """
        SELECT 
            p.MaPhong,
            p.TenPhong,
            p.MaTrangThai,
            t.TenTrangThai,
            p.SoDienHienTai,
            p.SoNuocHienTai,
            p.GiaThueNgay,
            p.GiaThueThang
        FROM Phong p
        INNER JOIN TrangThaiPhong t
            ON p.MaTrangThai = t.MaTrangThai
        WHERE p.MaTrangThai = ?
    """;

        List<Phong> lstPhong = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            // Sử dụng hằng số TRANG_THAI_TRONG (giá trị là 1)
            ps.setInt(1, TRANG_THAI_TRONG);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Phong p = new Phong();
                    p.setMaPhong(rs.getInt("MaPhong"));
                    p.setTenPhong(rs.getString("TenPhong"));
                    p.setMaTrangThai(rs.getInt("MaTrangThai"));
                    p.setTenTrangThai(rs.getString("TenTrangThai"));
                    p.setSoDienHienTai(rs.getInt("SoDienHienTai"));
                    p.setSoNuocHienTai(rs.getInt("SoNuocHienTai"));
                    p.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                    p.setGiaThueThang(rs.getDouble("GiaThueThang"));

                    lstPhong.add(p);
                }
            }
        }
        return lstPhong;
    }

    public int insertPhong(Connection conn, Phong p) throws SQLException {
        String sql = """
        INSERT INTO Phong (
            TenPhong,
            MaTrangThai,
            GiaThueNgay,
            GiaThueThang
        ) VALUES (?, ?, ?, ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getMaTrangThai());
            ps.setDouble(3, p.getGiaThueNgay());
            ps.setDouble(4, p.getGiaThueThang());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // MaPhong
                }
            }
        }
        return -1;
    }

    public boolean updatePhong(Connection conn, Phong p) throws SQLException {
        String sql = """
        UPDATE Phong
        SET
            TenPhong = ?,
            MaTrangThai = ?,
            GiaThueNgay = ?,
            GiaThueThang = ?
        WHERE MaPhong = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getMaTrangThai());
            ps.setDouble(3, p.getGiaThueNgay());
            ps.setDouble(4, p.getGiaThueThang());
            ps.setInt(5, p.getMaPhong());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePhong(Connection conn, int maPhong) throws SQLException {
        String sql = """
        UPDATE Phong
        SET MaTrangThai = ?
        WHERE MaPhong = ?
          AND MaTrangThai <> ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, TRANG_THAI_DA_XOA);
            ps.setInt(2, maPhong);
            ps.setInt(3, TRANG_THAI_DA_XOA);

            return ps.executeUpdate() > 0;
        }
    }

    public int getMaTrangThaiByMaPhong(Connection conn, int maPhong) throws SQLException {
        String sql = "SELECT MaTrangThai FROM Phong WHERE MaPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhong);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MaTrangThai");
                } else {
                    throw new SQLException("Không tìm thấy phòng với mã: " + maPhong);
                }
            }
        }
    }

    public boolean updateTrangThaiPhong(
            Connection conn, int maPhong, int maTrangThai
    ) throws SQLException {

        String sql = "UPDATE Phong SET MaTrangThai = ? WHERE MaPhong = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTrangThai);
            ps.setInt(2, maPhong);

            return ps.executeUpdate() > 0;
        }
    }
}
