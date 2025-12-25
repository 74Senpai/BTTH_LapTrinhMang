package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.HopDongThue;

public class HopDongThueDAO {

    public HopDongThue create(Connection conn, HopDongThue hd) throws SQLException {
        String sql = """
            INSERT INTO HopDongThue
            (MaKhachHang, MaPhong, MaNhanVien, LoaiHinhThue,
             NgayBatDau, NgayKetThuc, TrangThaiHopDong)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, hd.getMaKhachHang());
            ps.setInt(2, hd.getMaPhong());
            ps.setInt(3, hd.getMaNhanVien());
            ps.setString(4, hd.getLoaiHinhThue());
            ps.setDate(5, Date.valueOf(hd.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(hd.getNgayKetThuc()));
            ps.setString(7, hd.getTrangThaiHopDong());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    hd.setMaHopDong(rs.getInt(1));
                }
            }
            return hd;
        }
    }

    public HopDongThue findById(Connection conn, int maHopDong) throws SQLException {
        String sql = "SELECT * FROM HopDongThue WHERE MaHopDong = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maHopDong);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<HopDongThue> findAll(Connection conn) throws SQLException {

        String sql = """
            SELECT 
                hd.MaHopDong,
                hd.MaKhachHang,
                hd.MaPhong,
                hd.MaNhanVien,
                hd.LoaiHinhThue,
                hd.NgayBatDau,
                hd.NgayKetThuc,
                hd.TrangThaiHopDong,

                kh.HoTen        AS TenKhachHang,
                kh.SoDienThoai  AS SoDienThoai,
                kh.CCCD         AS CCCD,

                p.TenPhong      AS TenPhong
            FROM HopDongThue hd
            JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKH
            JOIN Phong p ON hd.MaPhong = p.MaPhong
            WHERE hd.TrangThaiHopDong <> 'Cancelled'
            ORDER BY hd.MaHopDong DESC
        """;

        List<HopDongThue> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HopDongThue hd = new HopDongThue();

                /* ===== ENTITY ===== */
                hd.setMaHopDong(rs.getInt("MaHopDong"));
                hd.setMaKhachHang(rs.getInt("MaKhachHang"));
                hd.setMaPhong(rs.getInt("MaPhong"));
                hd.setMaNhanVien(rs.getObject("MaNhanVien", Integer.class));
                hd.setLoaiHinhThue(rs.getString("LoaiHinhThue"));
                hd.setNgayBatDau(rs.getDate("NgayBatDau").toLocalDate());
                hd.setNgayKetThuc(rs.getDate("NgayKetThuc").toLocalDate());
                hd.setTrangThaiHopDong(rs.getString("TrangThaiHopDong"));

                /* ===== VIEW FIELD ===== */
                hd.setTenKhachHang(rs.getString("TenKhachHang"));
                hd.setSoDienThoai(rs.getString("SoDienThoai"));
                hd.setCccd(rs.getString("CCCD"));
                hd.setTenPhong(rs.getString("TenPhong"));

                list.add(hd);
            }
        }
        return list;
    }

    public List<HopDongThue> findByPhong(Connection conn, int maPhong) throws SQLException {
        String sql = "SELECT * FROM HopDongThue WHERE MaPhong = ?";
        List<HopDongThue> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhong);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void update(Connection conn, HopDongThue hd) throws SQLException {
        String sql = """
            UPDATE HopDongThue SET
                MaKhachHang = ?,
                MaPhong = ?,
                MaNhanVien = ?,
                LoaiHinhThue = ?,
                NgayBatDau = ?,
                NgayKetThuc = ?,
                TrangThaiHopDong = ?
            WHERE MaHopDong = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hd.getMaKhachHang());
            ps.setInt(2, hd.getMaPhong());
            ps.setInt(3, hd.getMaNhanVien());
            ps.setString(4, hd.getLoaiHinhThue());
            ps.setDate(5, Date.valueOf(hd.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(hd.getNgayKetThuc()));
            ps.setString(7, hd.getTrangThaiHopDong());
            ps.setInt(8, hd.getMaHopDong());

            ps.executeUpdate();
        }
    }

    public void cancel(Connection conn, int maHopDong) throws SQLException {
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

    private HopDongThue mapRow(ResultSet rs) throws SQLException {
        HopDongThue hd = new HopDongThue();
        hd.setMaHopDong(rs.getInt("MaHopDong"));
        hd.setMaKhachHang(rs.getInt("MaKhachHang"));
        hd.setMaPhong(rs.getInt("MaPhong"));
        hd.setMaNhanVien(rs.getInt("MaNhanVien"));
        hd.setLoaiHinhThue(rs.getString("LoaiHinhThue"));
        hd.setNgayBatDau(rs.getDate("NgayBatDau").toLocalDate());
        hd.setNgayKetThuc(rs.getDate("NgayKetThuc").toLocalDate());
        hd.setTrangThaiHopDong(rs.getString("TrangThaiHopDong"));
        return hd;
    }
}
