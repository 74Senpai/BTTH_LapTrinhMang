package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.HoaDonDAO;
import homestay.Server.DTOs.HoaDonDTO;
import homestay.Server.Models.HoaDon;

public class HoaDonService {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    /**
     * LẤY DANH SÁCH TẤT CẢ HÓA ĐƠN
     * Chuyển đổi từ Model (chứa dữ liệu JOIN) sang DTO View
     */
    public HoaDonDTO.ListHoaDon getAllHoaDon() {
        HoaDonDTO.ListHoaDon result = new HoaDonDTO.ListHoaDon();
        try (Connection conn = DBConnection.getConnection()) {
            List<HoaDon> list = hoaDonDAO.getAllHoaDon(conn);

            for (HoaDon hd : list) {
                HoaDonDTO.View view = new HoaDonDTO.View(
                        Integer.parseInt(hd.getMaThanhToan()),
                        Integer.parseInt(hd.getMaHopDong()),
                        hd.getMaDienNuoc(),
                        hd.getTienPhong(),
                        hd.getTienChiPhiPhu(),
                        hd.getTongTien(),
                        hd.getNgayThanhToan() != null ? hd.getNgayThanhToan().toString() : "",
                        hd.getTenKhachHang(),
                        hd.getTenPhong()
                );
                result.addHoaDon(view);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
    }

    /**
     * LẤY CHI TIẾT MỘT HÓA ĐƠN THEO ID
     */
    public HoaDonDTO.View getHoaDonById(int maThanhToan) {
        try (Connection conn = DBConnection.getConnection()) {
            HoaDon hd = hoaDonDAO.getHoaDonById(conn, maThanhToan);
            if (hd == null) return null;

            return new HoaDonDTO.View(
                    Integer.parseInt(hd.getMaThanhToan()),
                    Integer.parseInt(hd.getMaHopDong()),
                    hd.getMaDienNuoc(),
                    hd.getTienPhong(),
                    hd.getTienChiPhiPhu(),
                    hd.getTongTien(),
                    hd.getNgayThanhToan().toString(),
                    hd.getTenKhachHang(),
                    hd.getTenPhong()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI HÓA ĐƠN
     * Thường gọi khi khách trả phòng hoặc chốt tiền tháng
     */
    public void createHoaDon(HoaDonDTO.Create dto) {
        try (Connection conn = DBConnection.getConnection()) {
            HoaDon hd = new HoaDon();
            hd.setMaHopDong(String.valueOf(dto.getMaHopDong()));
            hd.setMaDienNuoc(dto.getMaDienNuoc());
            hd.setTienPhong(dto.getTienPhong());
            hd.setTienChiPhiPhu(dto.getTienChiPhiPhu());
            hd.setTongTien(dto.getTongTien());

            int idMoi = hoaDonDAO.insertHoaDon(conn, hd);
            if (idMoi == -1) {
                throw new RuntimeException("Không thể tạo hóa đơn");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HÓA ĐƠN
     */
    public void updateHoaDon(HoaDonDTO.Update dto) {
        try (Connection conn = DBConnection.getConnection()) {
            // Lấy dữ liệu cũ lên trước
            HoaDon hd = hoaDonDAO.getHoaDonById(conn, dto.getMaThanhToan());
            if (hd == null) throw new RuntimeException("Hóa đơn không tồn tại");

            // Cập nhật các trường cho phép sửa
            hd.setTienChiPhiPhu(dto.getTienChiPhiPhu());
            hd.setTongTien(dto.getTongTien());

            boolean success = hoaDonDAO.updateHoaDon(conn, hd);
            if (!success) throw new RuntimeException("Cập nhật hóa đơn thất bại");
            
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    /**
     * XÓA HÓA ĐƠN
     */
    public void deleteHoaDon(int maThanhToan) {
        try (Connection conn = DBConnection.getConnection()) {
            boolean success = hoaDonDAO.deleteHoaDon(conn, maThanhToan);
            if (!success) throw new RuntimeException("Xóa hóa đơn thất bại");
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }
}