package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;

import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.ThongKeDAO;
import homestay.Server.DTOs.ThongKeDTO;

public class ThongKeService {

    private final static ThongKeDAO thongKeDAO = new ThongKeDAO();

    public ThongKeDTO.BaoCaoTongHop getFullBaoCao(int nam) {
        ThongKeDTO.BaoCaoTongHop baoCao = new ThongKeDTO.BaoCaoTongHop();
        try (Connection conn = DBConnection.getConnection()) {
            baoCao.dsDoanhThu = thongKeDAO.getDoanhThuTheoThang(conn, nam);
            baoCao.dsTrangThai = thongKeDAO.getThongKeTrangThaiPhong(conn);
            baoCao.tongKhachDangThue = thongKeDAO.getTongKhachDangThue(conn);
            baoCao.doanhThuThangNay = thongKeDAO.getDoanhThuThangNay(conn);
            return baoCao;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thống kê: " + e.getMessage());
        }
    }
}
