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
            throw new RuntimeException("Lỗi khi lấy danh sách hợp đồng", e);
        }
    }

    public void createHopDong(int maNhanVien, HopDongDTO.Create dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu không được null");
        }

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                // 1. Xử lý khách hàng
                KhachHang kh = khachHangDAO.findByCCCD(conn, dto.cccd());

                if (kh == null) {
                    kh = new KhachHang();
                    kh.setHoTen(dto.tenKhachHang());
                    kh.setSoDienThoai(dto.soDienThoai());
                    kh.setCccd(dto.cccd());
                    khachHangDAO.insert(conn, kh);

                    kh = khachHangDAO.findByCCCD(conn, dto.cccd());
                } else {
                    kh.setHoTen(dto.tenKhachHang());
                    kh.setSoDienThoai(dto.soDienThoai());
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

                // 3. Cập nhật trạng thái phòng
                phongDAO.updateTrangThaiPhong(conn, dto.maPhong(), PhongDAO.TRANG_THAI_SU_DUNG);

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống khi tạo hợp đồng", e);
        }
    }

    public void updateHopDong(HopDongDTO.Update dto) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                HopDongThue hdOld = hopDongThueDAO.findById(conn, dto.maHopDong());
                if (hdOld == null) {
                    throw new RuntimeException("Không tìm thấy hợp đồng");
                }

                int maPhongCu = hdOld.getMaPhong();
                int maPhongMoi = dto.maPhong();

                // Cập nhật khách hàng
                KhachHang kh = new KhachHang();
                kh.setMaKH(hdOld.getMaKhachHang());
                kh.setHoTen(dto.tenKhachHang());
                kh.setSoDienThoai(dto.soDienThoai());
                kh.setCccd(dto.cccd());
                khachHangDAO.update(conn, kh);

                // Cập nhật hợp đồng
                hdOld.setMaPhong(maPhongMoi);
                hdOld.setLoaiHinhThue(dto.loaiHinhThue());
                hopDongThueDAO.update(conn, hdOld);

                // Đổi phòng
                if (maPhongCu != maPhongMoi) {
                    phongDAO.updateTrangThaiPhong(conn, maPhongCu, PhongDAO.TRANG_THAI_TRONG); // trống
                    phongDAO.updateTrangThaiPhong(conn, maPhongMoi, PhongDAO.TRANG_THAI_SU_DUNG); // đang sử dụng
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật hợp đồng", e);
        }
    }

    public void deleteHopDong(int maHopDong) {

        try (Connection conn = DBConnection.getConnection()) {
            hopDongThueDAO.cancel(conn, maHopDong);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi hủy hợp đồng", e);
        }
    }
}
