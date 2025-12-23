package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.DTOs.HoaDonDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.HoaDonDAO;
import homestay.Server.Models.HoaDon;

public class HoaDonService {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    /**
     * LẤY DANH SÁCH TẤT CẢ HÓA ĐƠN
     * Chuyển đổi từ Model (chứa dữ liệu JOIN) sang DTO View
     */
    public HoaDonDTO.ListHoaDon getAllHoaDon() {
        List<HoaDonDTO.View> res = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            List<HoaDon> list = hoaDonDAO.getAllHoaDon(conn);

            for (HoaDon hd : list) {
                HoaDonDTO.View view = new HoaDonDTO.View(
                        hd.getMaThanhToan(),
                        hd.getMaHopDong(),
                        hd.getMaDienNuoc(),
                        hd.getTienPhong(),
                        hd.getTienChiPhiPhu(),
                        hd.getTongTien(),
                        hd.getNgayThanhToan() != null ? hd.getNgayThanhToan().toString() : "",
                        hd.getTenKhachHang(),
                        hd.getTenPhong(),
                        hd.getTrangThaiThanhToan()
                );
                res.add(view);
            }
            return new HoaDonDTO.ListHoaDon(res);
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
                    hd.getMaThanhToan(),
                    hd.getMaHopDong(),
                    hd.getMaDienNuoc(),
                    hd.getTienPhong(),
                    hd.getTienChiPhiPhu(),
                    hd.getTongTien(),
                    hd.getNgayThanhToan().toString(),
                    hd.getTenKhachHang(),
                    hd.getTenPhong(),
                    hd.getTrangThaiThanhToan()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }
    }

    // Đơn giá quy định
    private final double GIA_DIEN = 3500;
    private final double GIA_NUOC = 12000;

    /**
     * TẠO MỚI HÓA ĐƠN
     */
    public void createHoaDon(HoaDonDTO.Create dto) {
        try (Connection conn = DBConnection.getConnection()) {
            HoaDon hd = new HoaDon();
            hd.setMaHopDong(dto.maHopDong());
            hd.setMaDienNuoc(dto.maDienNuoc());
            hd.setTrangThaiThanhToan(dto.trangThaiThanhToan());

            // 1. Gọi DAO lấy tiền phòng
            double tienPhong = hoaDonDAO.getGiaThueTuHopDong(conn, dto.maHopDong());
            
            // 2. Gọi DAO lấy tiền điện nước (nếu có)
            double tienDienNuoc = 0;
            if (dto.maDienNuoc() != null) {
                tienDienNuoc = hoaDonDAO.getTienDienNuocTuChiSo(conn, dto.maDienNuoc(), GIA_DIEN, GIA_NUOC);
            }

            // 3. Thiết lập các giá trị
            hd.setTienPhong(tienPhong);
            // Phụ phí thực tế = Phụ phí khách nhập + Tiền điện nước
            hd.setTienChiPhiPhu(dto.tienChiPhiPhu() + tienDienNuoc);
            // Tổng tiền = Tiền phòng + Phụ phí tổng
            hd.setTongTien(tienPhong + hd.getTienChiPhiPhu());

            if (hoaDonDAO.insertHoaDon(conn, hd) == -1) {
                throw new RuntimeException("Lỗi khi thêm hóa đơn vào DB");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HÓA ĐƠN
     */
    public void updateHoaDon(HoaDonDTO.Update dto) {
        try (Connection conn = DBConnection.getConnection()) {
            // Lấy dữ liệu hiện tại từ DB
            HoaDon hd = hoaDonDAO.getHoaDonById(conn, dto.maThanhToan());
            if (hd == null) throw new RuntimeException("Không tìm thấy hóa đơn");

            // Cập nhật các thông tin thay đổi
            hd.setTienChiPhiPhu(dto.tienChiPhiPhu());
            hd.setTrangThaiThanhToan(dto.trangThaiThanhToan());
            
            // Tính lại tổng dựa trên tiền phòng đã có sẵn trong DB
            hd.setTongTien(hd.getTienPhong() + dto.tienChiPhiPhu());

            if (!hoaDonDAO.updateHoaDon(conn, hd)) {
                throw new RuntimeException("Cập nhật thất bại");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
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