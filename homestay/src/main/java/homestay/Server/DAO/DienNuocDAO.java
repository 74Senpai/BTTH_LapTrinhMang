package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.Models.DienNuoc;

public class DienNuocDAO {


    public List<DienNuoc> getAll(Connection conn) throws SQLException {
        // Query lấy dữ liệu và sắp xếp theo thời gian mới nhất
        String sql = """
            SELECT dn.*, p.TenPhong 
            FROM DienNuocHangThang dn
            JOIN Phong p ON dn.MaPhong = p.MaPhong
            ORDER BY dn.Nam DESC, dn.Thang DESC
        """;

        List<DienNuoc> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Sử dụng hàm map dữ liệu để tránh lặp code
                list.add(mapResultSetToDienNuoc(rs));
            }
        }
        return list;
    }

    /**
     * Phương thức cập nhật chỉ số điện nước hiện tại của PHÒNG
     * Yêu cầu: Không cập nhật hóa đơn
     */
    public boolean updateChiSoHienTaiCuaPhong(Connection conn, int maPhong, int soDienMoi, int soNuocMoi) throws SQLException {
        String sql = """
            UPDATE Phong 
            SET SoDienHienTai = ?, 
                SoNuocHienTai = ? 
            WHERE MaPhong = ?
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soDienMoi);
            ps.setInt(2, soNuocMoi);
            ps.setInt(3, maPhong);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Thêm mới bản ghi điện nước hàng tháng
     */
    public boolean insertDienNuocHangThang(Connection conn, DienNuoc dn) throws SQLException {
        String sql = """
            INSERT INTO DienNuocHangThang (
                MaPhong, Thang, Nam, 
                ChiSoDienCu, ChiSoDienMoi, 
                ChiSoNuocCu, ChiSoNuocMoi
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dn.getMaPhong());
            ps.setInt(2, dn.getThang());
            ps.setInt(3, dn.getNam());
            ps.setInt(4, dn.getChiSoDienCu());
            ps.setInt(5, dn.getChiSoDienMoi());
            ps.setInt(6, dn.getChiSoNuocCu());
            ps.setInt(7, dn.getChiSoNuocMoi());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật chỉ số cho bản ghi điện nước đã tồn tại
     */
    public boolean updateChiSoDienNuoc(Connection conn, int maDienNuoc, int soDienMoi, int soNuocMoi) throws SQLException {
        String sql = """
            UPDATE DienNuocHangThang 
            SET ChiSoDienMoi = ?, 
                ChiSoNuocMoi = ? 
            WHERE MaDienNuoc = ?
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soDienMoi);
            ps.setInt(2, soNuocMoi);
            ps.setInt(3, maDienNuoc);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy chỉ số mới nhất của một phòng để làm chỉ số cũ cho tháng sau
     */
    public DienNuoc getChiSoMoiNhatByPhong(Connection conn, int maPhong) throws SQLException {
        String sql = "SELECT * FROM DienNuocHangThang WHERE MaPhong = ? ORDER BY Nam DESC, Thang DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDienNuoc(rs);
                }
            }
        }
        return null;
    }

    private DienNuoc mapResultSetToDienNuoc(ResultSet rs) throws SQLException {
        DienNuoc dn = new DienNuoc();
        dn.setMaDienNuoc(rs.getInt("MaDienNuoc"));
        dn.setMaPhong(rs.getInt("MaPhong"));
        dn.setThang(rs.getInt("Thang"));
        dn.setNam(rs.getInt("Nam"));
        dn.setChiSoDienCu(rs.getInt("ChiSoDienCu"));
        dn.setChiSoDienMoi(rs.getInt("ChiSoDienMoi"));
        dn.setChiSoNuocCu(rs.getInt("ChiSoNuocCu"));
        dn.setChiSoNuocMoi(rs.getInt("ChiSoNuocMoi"));
        dn.setSoDienTieuThu(rs.getInt("SoDienTieuThu"));
        dn.setSoNuocTieuThu(rs.getInt("SoNuocTieuThu"));
        return dn;
    }

    public DienNuoc getDienNuocById(Connection conn, int maDienNuoc) throws SQLException {
        String sql = "SELECT * FROM DienNuocHangThang WHERE MaDienNuoc = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDienNuoc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DienNuoc dn = new DienNuoc();
                    dn.setMaDienNuoc(rs.getInt("MaDienNuoc"));
                    dn.setMaPhong(rs.getInt("MaPhong"));
                    dn.setThang(rs.getInt("Thang"));
                    dn.setNam(rs.getInt("Nam"));
                    dn.setChiSoDienCu(rs.getInt("ChiSoDienCu"));
                    dn.setChiSoDienMoi(rs.getInt("ChiSoDienMoi"));
                    
                    // Lấy các cột ảo (Virtual Columns) do DB tự tính toán
                    dn.setSoDienTieuThu(rs.getInt("SoDienTieuThu"));
                    
                    dn.setChiSoNuocCu(rs.getInt("ChiSoNuocCu"));
                    dn.setChiSoNuocMoi(rs.getInt("ChiSoNuocMoi"));
                    
                    // Lấy các cột ảo (Virtual Columns) do DB tự tính toán
                    dn.setSoNuocTieuThu(rs.getInt("SoNuocTieuThu"));
                    
                    return dn;
                }
            }
        }
        return null;
    }
}