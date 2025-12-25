package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import homestay.DTOs.HopDongDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.HopDongThueDAO;
import homestay.Server.DAO.KhachHangDAO;
import homestay.Server.DAO.PhongDAO;
import homestay.Server.Models.HopDongThue;
import homestay.Server.Models.KhachHang;

public class HopDongThueService {

    private final HopDongThueDAO hopDongThueDAO = new HopDongThueDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final PhongDAO phongDAO = new PhongDAO();

    /**
     * LẤY DANH SÁCH TẤT CẢ HỢP ĐỒNG
     */
    public HopDongDTO.ListHopDong getAllHopDong() {
        HopDongDTO.ListHopDong result = new HopDongDTO.ListHopDong();
        try (Connection conn = DBConnection.getConnection()) {
            List<HopDongThue> list = hopDongThueDAO.findAll(conn);
            for (HopDongThue hd : list) {
                result.addHopDong(new HopDongDTO.View(
                        hd.getMaHopDong(),
                        hd.getTenKhachHang(),
                        hd.getSoDienThoai(),
                        hd.getCccd(),
                        hd.getTenPhong(),
                        hd.getNgayBatDau().toString(),
                        hd.getNgayKetThuc().toString(),
                        hd.getLoaiHinhThue()
                ));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống khi lấy danh sách hợp đồng: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI HỢP ĐỒNG
     */
    public void createHopDong(int maNhanVien, HopDongDTO.Create dto) {
        // --- VALIDATE DỮ LIỆU ĐẦU VÀO ---
        if (dto == null) {
            throw new RuntimeException("Dữ liệu hợp đồng không được để trống.");
        }
        if (maNhanVien <= 0) {
            throw new RuntimeException("Nhân viên thực hiện không hợp lệ.");
        }
        if (dto.maPhong() <= 0) {
            throw new RuntimeException("Mã phòng không hợp lệ.");
        }

        if (dto.tenKhachHang() == null || dto.tenKhachHang().trim().isEmpty()) {
            throw new RuntimeException("Tên khách hàng không được để trống.");
        }
        if (dto.soDienThoai() == null || !dto.soDienThoai().matches("\\d{10,11}")) {
            throw new RuntimeException("Số điện thoại khách hàng phải từ 10-11 chữ số.");
        }
        if (dto.cccd() == null || !dto.cccd().matches("\\d{12}")) {
            throw new RuntimeException("Số CCCD phải bao gồm 12 chữ số.");
        }

        if (dto.ngayBatDau() == null || dto.ngayKetThuc() == null) {
            throw new RuntimeException("Ngày bắt đầu và ngày kết thúc không được để trống.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Xử lý khách hàng
                KhachHang kh = khachHangDAO.findByCCCD(conn, dto.cccd());

                if (kh == null) {
                    kh = new KhachHang();
                    kh.setHoTen(dto.tenKhachHang().trim());
                    kh.setSoDienThoai(dto.soDienThoai().trim());
                    kh.setCccd(dto.cccd().trim());
                    khachHangDAO.insert(conn, kh);
                    // Lấy lại đối tượng sau khi insert để có MaKH
                    kh = khachHangDAO.findByCCCD(conn, dto.cccd());
                } else {
                    // Cập nhật lại thông tin mới nhất của khách dựa trên CCCD
                    kh.setHoTen(dto.tenKhachHang().trim());
                    kh.setSoDienThoai(dto.soDienThoai().trim());
                    khachHangDAO.update(conn, kh);
                }

                // 2. Tạo hợp đồng
                HopDongThue hd = new HopDongThue();
                hd.setMaKhachHang(kh.getMaKH());
                hd.setMaPhong(dto.maPhong());
                hd.setLoaiHinhThue(dto.loaiHinhThue());
                hd.setMaNhanVien(maNhanVien);
                hd.setNgayBatDau(LocalDate.parse(dto.ngayBatDau()));
                hd.setNgayKetThuc(LocalDate.parse(dto.ngayKetThuc()));
                hd.setTrangThaiHopDong("Active");

                hopDongThueDAO.create(conn, hd);

                // 3. Cập nhật trạng thái phòng sang "Đang sử dụng" (MaTrangThai = 2)
                phongDAO.updateTrangThaiPhong(conn, dto.maPhong(), PhongDAO.TRANG_THAI_SU_DUNG);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Lỗi tạo hợp đồng: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HỢP ĐỒNG
     */
    public void updateHopDong(HopDongDTO.Update dto) {
        // --- VALIDATE DỮ LIỆU ĐẦU VÀO ---
        if (dto == null) {
            throw new RuntimeException("Dữ liệu cập nhật không được để trống.");
        }
        if (dto.maHopDong() <= 0) {
            throw new RuntimeException("Mã hợp đồng không hợp lệ.");
        }
        if (dto.maPhong() <= 0) {
            throw new RuntimeException("Mã phòng mới không hợp lệ.");
        }

        if (dto.tenKhachHang() == null || dto.tenKhachHang().trim().isEmpty()) {
            throw new RuntimeException("Tên khách hàng không được để trống.");
        }
        if (dto.soDienThoai() == null || !dto.soDienThoai().matches("\\d{10,11}")) {
            throw new RuntimeException("Số điện thoại không hợp lệ.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                HopDongThue hdOld = hopDongThueDAO.findById(conn, dto.maHopDong());
                if (hdOld == null) {
                    throw new RuntimeException("Không tìm thấy hợp đồng mã số: " + dto.maHopDong());
                }

                int maPhongCu = hdOld.getMaPhong();
                int maPhongMoi = dto.maPhong();

                // Cập nhật thông tin khách hàng gắn với hợp đồng này
                KhachHang kh = new KhachHang();
                kh.setMaKH(hdOld.getMaKhachHang());
                kh.setHoTen(dto.tenKhachHang().trim());
                kh.setSoDienThoai(dto.soDienThoai().trim());
                kh.setCccd(dto.cccd().trim());
                khachHangDAO.update(conn, kh);

                // Cập nhật thông tin hợp đồng
                hdOld.setMaPhong(maPhongMoi);
                hdOld.setLoaiHinhThue(dto.loaiHinhThue());
                hopDongThueDAO.update(conn, hdOld);

                // Nếu có sự thay đổi phòng, cập nhật trạng thái cả 2 phòng
                if (maPhongCu != maPhongMoi) {
                    phongDAO.updateTrangThaiPhong(conn, maPhongCu, PhongDAO.TRANG_THAI_TRONG); // Về Trống (1)
                    phongDAO.updateTrangThaiPhong(conn, maPhongMoi, PhongDAO.TRANG_THAI_SU_DUNG); // Sang Sử dụng (2)
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Lỗi cập nhật hợp đồng: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    /**
     * HỦY HỢP ĐỒNG (Cancel)
     */
    public void deleteHopDong(int maHopDong) {
        // --- VALIDATE ---
        if (maHopDong <= 0) {
            throw new RuntimeException("Mã hợp đồng cần hủy không hợp lệ.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Hàm cancel trong DAO đã bao gồm logic cập nhật trạng thái phòng bằng JOIN
            hopDongThueDAO.cancel(conn, maHopDong);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi hủy hợp đồng: " + e.getMessage());
        }
    }
}
