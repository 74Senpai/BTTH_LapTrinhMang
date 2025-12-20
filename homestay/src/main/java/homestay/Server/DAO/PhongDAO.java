package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.Phong;

public class PhongDAO {

    public List<Phong> getAllPhong() throws SQLException {
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
    """;

        List<Phong> lstPhong = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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

                lstPhong.add(p);
            }
        }

        return lstPhong;
    }

    public boolean insertPhong(Phong p) throws SQLException {
        String sql = """
        INSERT INTO Phong (
            TenPhong,
            MaTrangThai,
            SoDienHienTai,
            SoNuocHienTai,
            GiaThueNgay,
            GiaThueThang
        ) VALUES (?, ?, 0, 0, ?, ?)
    """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getMaTrangThai());
            ps.setDouble(3, p.getGiaThueNgay());
            ps.setDouble(4, p.getGiaThueThang());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePhong(Phong p) throws SQLException {
        String sql = """
        UPDATE Phong
        SET
            TenPhong = ?,
            MaTrangThai = ?,
            GiaThueNgay = ?,
            GiaThueThang = ?
        WHERE MaPhong = ?
    """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getMaTrangThai());
            ps.setDouble(3, p.getGiaThueNgay());
            ps.setDouble(4, p.getGiaThueThang());
            ps.setString(5, p.getMaPhong());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePhong(String maPhong) throws SQLException {
        String sql = "DELETE FROM Phong WHERE MaPhong = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;
        }
    }
}
