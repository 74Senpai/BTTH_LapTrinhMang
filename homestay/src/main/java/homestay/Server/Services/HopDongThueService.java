package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.HopDongThueDAO;
import homestay.Server.DAO.KhachHangDAO;
import homestay.Server.DAO.PhongDAO;
import homestay.Server.DTOs.HopDongDTO;
import homestay.Server.Models.HopDongThue;
import homestay.Server.Models.KhachHang;

public class HopDongThueService {

    private final HopDongThueDAO hopDongThueDAO = new HopDongThueDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final PhongDAO phongDAO = new PhongDAO();

    /**
     * LẤY DANH SÁCH HỢP ĐỒNG (Trả về ListHopDongThue DTO)
     */
    public HopDongDTO.ListHopDong getAllHopDong() {
        HopDongDTO.ListHopDong result = new HopDongDTO.ListHopDong();
        try (Connection conn = DBConnection.getConnection()) {
            List<HopDongThue> list = hopDongThueDAO.findAll(conn);

            for (HopDongThue hd : list) {
                // Map từ Model sang DTO View
                HopDongDTO.View view = new HopDongDTO.View(
                        hd.getMaHopDong(),
                        hd.getTenKhachHang(),
                        hd.getSoDienThoai(),
                        hd.getCccd(),
                        hd.getTenPhong(),
                        hd.getNgayBatDau().toString(),
                        hd.getNgayKetThuc().toString(),
                        hd.getLoaiHinhThue()
                );
                result.addHopDong(view);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách hợp đồng: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI HỢP ĐỒNG (Xử lý 2 bảng KhachHang và HopDongThue)
     */
    public void createHopDong(int MaNhanVien, HopDongDTO.Create dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu không được null");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            KhachHang kh = khachHangDAO.findByCCCD(conn, dto.getCccd());
            int maKH;
            if (kh == null) {
                kh = new KhachHang();
                kh.setHoTen(dto.getTenKhachHang());
                kh.setSoDienThoai(dto.getSoDienThoai());
                kh.setCccd(dto.getCccd());
                khachHangDAO.insert(conn, kh);

                kh = khachHangDAO.findByCCCD(conn, dto.getCccd());
            } else {
                // Cập nhật thông tin nếu có thay đổi
                kh.setHoTen(dto.getTenKhachHang());
                kh.setSoDienThoai(dto.getSoDienThoai());
                khachHangDAO.update(conn, kh);
            }
            maKH = kh.getMaKH();

            // 2. Tạo hợp đồng thuê
            HopDongThue hd = new HopDongThue();
            hd.setMaKhachHang(maKH);
            hd.setMaPhong(dto.getMaPhongDangThue());
            hd.setLoaiHinhThue(dto.getLoaiHinhThue());
            hd.setMaNhanVien(MaNhanVien);
            hd.setNgayBatDau(LocalDate.parse(dto.getNgayBatDau()));
            hd.setNgayKetThuc(LocalDate.parse(dto.getNgayKetThuc()));
            hd.setTrangThaiHopDong("Active");

            hopDongThueDAO.create(conn, hd);

            phongDAO.updateTrangThaiPhong(conn, dto.getMaPhongDangThue(), PhongDAO.TRANG_THAI_SU_DUNG);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Lỗi hệ thống khi tạo hợp đồng: " + e.getMessage());
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * CẬP NHẬT HỢP ĐỒNG
     */
    public void updateHopDong(HopDongDTO.Update dto) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Tìm hợp đồng cũ để lấy MaKH
            HopDongThue hdOld = hopDongThueDAO.findById(conn, dto.getMaHopDong());
            if (hdOld == null) {
                throw new RuntimeException("Không tìm thấy hợp đồng");
            }

            int maPhongCu = hdOld.getMaPhong();
            int maPhongMoi = dto.getMaPhongDangThue();

            // 2. Cập nhật thông tin khách hàng
            KhachHang kh = new KhachHang();
            kh.setMaKH(hdOld.getMaKhachHang());
            kh.setHoTen(dto.getTenKhachHang());
            kh.setSoDienThoai(dto.getSoDienThoai());
            kh.setCccd(dto.getCccd());
            khachHangDAO.update(conn, kh);

            // 3. Cập nhật thông tin hợp đồng
            hdOld.setMaPhong(maPhongMoi);
            hdOld.setLoaiHinhThue(dto.getLoaiHinhThue());
            hopDongThueDAO.update(conn, hdOld);

            // 4. Nếu đổi phòng → cập nhật trạng thái phòng
            if (maPhongCu != maPhongMoi) {
                // phòng cũ → trống
                phongDAO.updateTrangThaiPhong(conn, maPhongCu, 1);

                // phòng mới → đang sử dụng
                phongDAO.updateTrangThaiPhong(conn, maPhongMoi, 2);
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Lỗi khi cập nhật hợp đồng: " + e.getMessage());
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * XÓA MỀM HỢP ĐỒNG (Hủy hợp đồng)
     */
    public void deleteHopDong(int maHopDong) {
        try (Connection conn = DBConnection.getConnection()) {
            // Sử dụng hàm xóa mềm có sẵn trong DAO của bạn
            hopDongThueDAO.cancel(conn, maHopDong);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi hủy hợp đồng: " + e.getMessage());
        }
    }
}
