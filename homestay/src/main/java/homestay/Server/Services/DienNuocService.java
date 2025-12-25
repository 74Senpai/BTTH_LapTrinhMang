package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import homestay.DTOs.DienNuocDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.DienNuocDAO;
import homestay.Server.DAO.PhongDAO;
import homestay.Server.Models.DienNuoc;
import homestay.Server.Models.Phong;

public class DienNuocService {

    private final DienNuocDAO dienNuocDAO = new DienNuocDAO();
    private final PhongDAO phongDAO = new PhongDAO();

    /**
     * LẤY DANH SÁCH CHỈ SỐ ĐIỆN NƯỚC HÀNG THÁNG
     */
    public DienNuocDTO.ListDienNuoc getAll() {
        List<DienNuocDTO.View> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            List<DienNuoc> list = dienNuocDAO.getAll(conn);

            for (DienNuoc dn : list) {
                result.add(new DienNuocDTO.View(
                        dn.getMaDienNuoc(),
                        dn.getMaPhong(),
                        dn.getThang(),
                        dn.getNam(),
                        dn.getChiSoDienCu(),
                        dn.getChiSoDienMoi(),
                        dn.getSoDienTieuThu(),
                        dn.getChiSoNuocCu(),
                        dn.getChiSoNuocMoi(),
                        dn.getSoNuocTieuThu()
                ));
            }
            return new DienNuocDTO.ListDienNuoc(result);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hệ thống khi truy xuất dữ liệu: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI BẢN GHI ĐIỆN NƯỚC
     */
    public void createPhieuThuDienNuoc(DienNuocDTO.Create dto) {
        // VALIDATE DỮ LIỆU ĐẦU VÀO
        if (dto == null) {
            throw new RuntimeException("Dữ liệu yêu cầu (DTO) không được null.");
        }
        if (dto.maPhong() <= 0) {
            throw new RuntimeException("Mã phòng không hợp lệ (phải là số dương).");
        }

        try (Connection conn = DBConnection.getConnection()) {
            Phong phong = phongDAO.getPhongById(conn, dto.maPhong());
            if (phong == null) {
                throw new RuntimeException("Phòng không tồn tại.");
            }

            LocalDate now = LocalDate.now();
            DienNuoc dn = new DienNuoc();
            dn.setMaPhong(dto.maPhong());
            dn.setThang(now.getMonthValue());
            dn.setNam(now.getYear());

            dn.setChiSoDienCu(phong.getSoDienHienTai());
            dn.setChiSoNuocCu(phong.getSoNuocHienTai());
            dn.setChiSoDienMoi(phong.getSoDienHienTai());
            dn.setChiSoNuocMoi(phong.getSoNuocHienTai());

            dienNuocDAO.insertDienNuocHangThang(conn, dn);

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi SQL khi tạo bản ghi: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT CHỈ SỐ
     */
    public void updateChiSoCuoiKy(DienNuocDTO.Update dto) {
        // VALIDATE DỮ LIỆU ĐẦU VÀO
        if (dto == null) {
            throw new RuntimeException("Dữ liệu cập nhật không được để trống.");
        }
        if (dto.maDienNuoc() <= 0) {
            throw new RuntimeException("ID bản ghi điện nước không hợp lệ.");
        }
        if (dto.chiSoDienMoi() < 0 || dto.chiSoNuocMoi() < 0) {
            throw new RuntimeException("Chỉ số mới không được là số âm.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            DienNuoc dnOld = dienNuocDAO.getDienNuocById(conn, dto.maDienNuoc());
            if (dnOld == null) {
                throw new RuntimeException("Không tìm thấy dữ liệu điện nước cần cập nhật.");
            }

            // Thực hiện cập nhật
            dienNuocDAO.updateChiSoDienNuoc(conn,
                    dto.maDienNuoc(),
                    dto.chiSoDienMoi(),
                    dto.chiSoNuocMoi()
            );

            dienNuocDAO.updateChiSoHienTaiCuaPhong(conn,
                    dnOld.getMaPhong(),
                    dto.chiSoDienMoi(),
                    dto.chiSoNuocMoi()
            );

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new RuntimeException("Lỗi hệ thống khi cập nhật: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
