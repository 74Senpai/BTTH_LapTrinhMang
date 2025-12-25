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

    // Đơn giá quy định
    private final double GIA_DIEN = 3500;   // 3.500đ / số
    private final double GIA_NUOC = 12000;  // 12.000đ / khối

    /**
     * LẤY DANH SÁCH TẤT CẢ HÓA ĐƠN
     */
    public HoaDonDTO.ListHoaDon getAllHoaDon() {
        List<HoaDonDTO.View> res = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            List<HoaDon> list = hoaDonDAO.getAllHoaDon(conn);

            for (HoaDon hd : list) {
                res.add(mapModelToView(hd));
            }
            return new HoaDonDTO.ListHoaDon(res);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống khi lấy danh sách hóa đơn: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI HÓA ĐƠN
     */
    public void createHoaDon(HoaDonDTO.Create dto) {
        // --- VALIDATE DỮ LIỆU ĐẦU VÀO ---
        if (dto == null) {
            throw new RuntimeException("Dữ liệu yêu cầu không được để trống.");
        }
        if (dto.maHopDong() <= 0) {
            throw new RuntimeException("Mã hợp đồng không hợp lệ.");
        }
        if (dto.tienChiPhiPhu() < 0) {
            throw new RuntimeException("Tiền chi phí phụ không được là số âm.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Tính tiền phòng (DAO check trạng thái Cancelled)
            double tienPhong;
            try {
                tienPhong = hoaDonDAO.calculateTienPhong(conn, dto.maHopDong());
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }

            // 2. Tính tiền điện nước (nếu có)
            double tienDienNuoc = 0;
            if (dto.maDienNuoc() != null && dto.maDienNuoc() > 0) {
                tienDienNuoc = hoaDonDAO.getTienDienNuocTuChiSo(conn, dto.maDienNuoc(), GIA_DIEN, GIA_NUOC);
            }

            // 3. Thiết lập Model
            HoaDon hd = new HoaDon();
            hd.setMaHopDong(dto.maHopDong());
            hd.setMaDienNuoc(dto.maDienNuoc());
            hd.setTienPhong(tienPhong);
            hd.setTienChiPhiPhu(dto.tienChiPhiPhu() + tienDienNuoc);
            hd.setTongTien(tienPhong + hd.getTienChiPhiPhu());
            hd.setTrangThaiThanhToan(dto.trangThaiThanhToan());

            // 4. Lưu vào DB
            if (hoaDonDAO.insertHoaDon(conn, hd) == -1) {
                throw new RuntimeException("Lỗi khi thêm hóa đơn vào cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HÓA ĐƠN
     */
    public void updateHoaDon(HoaDonDTO.Update dto) {
        // --- VALIDATE DỮ LIỆU ĐẦU VÀO ---
        if (dto == null) {
            throw new RuntimeException("Dữ liệu cập nhật không được để trống.");
        }
        if (dto.maThanhToan() <= 0) {
            throw new RuntimeException("Mã hóa đơn không hợp lệ.");
        }
        if (dto.tienChiPhiPhu() < 0) {
            throw new RuntimeException("Phụ phí không được là số âm.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Lấy thông tin hiện tại
            HoaDon hd = hoaDonDAO.getHoaDonById(conn, dto.maThanhToan());
            if (hd == null) {
                throw new RuntimeException("Hóa đơn không tồn tại trên hệ thống.");
            }

            // 2. Gán dữ liệu mới
            hd.setTienChiPhiPhu(dto.tienChiPhiPhu());
            hd.setTrangThaiThanhToan(dto.trangThaiThanhToan());
            hd.setTongTien(hd.getTienPhong() + dto.tienChiPhiPhu());

            // 3. Thực hiện cập nhật
            boolean success = hoaDonDAO.updateHoaDon(conn, hd);

            if (!success) {
                throw new RuntimeException("Không thể cập nhật! Hóa đơn có thể đã được thanh toán hoặc không tồn tại.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * XÓA HÓA ĐƠN
     */
    public void deleteHoaDon(int maThanhToan) {
        // --- VALIDATE DỮ LIỆU ĐẦU VÀO ---
        if (maThanhToan <= 0) {
            throw new RuntimeException("Mã hóa đơn không hợp lệ.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            boolean success = hoaDonDAO.deleteHoaDon(conn, maThanhToan);

            if (!success) {
                throw new RuntimeException("Không thể xóa! Hóa đơn đã được thanh toán hoặc không tồn tại.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống khi thực hiện xóa: " + e.getMessage());
        }
    }

    /**
     * CHI TIẾT HÓA ĐƠN
     */
    public HoaDonDTO.View getHoaDonById(int maThanhToan) {
        // --- VALIDATE ---
        if (maThanhToan <= 0) {
            throw new RuntimeException("Mã hóa đơn cần tìm không hợp lệ.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            HoaDon hd = hoaDonDAO.getHoaDonById(conn, maThanhToan);
            return hd != null ? mapModelToView(hd) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn chi tiết: " + e.getMessage());
        }
    }

    // Helper map Model sang DTO
    private HoaDonDTO.View mapModelToView(HoaDon hd) {
        return new HoaDonDTO.View(
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
    }
}
