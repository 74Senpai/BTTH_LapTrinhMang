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
    public List<DienNuocDTO.View> getAll() {
        List<DienNuocDTO.View> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            List<DienNuoc> list = dienNuocDAO.getAll(conn);

            for (DienNuoc dn : list) {
                // Map từ Model sang DTO View (sử dụng Constructor đã định nghĩa trong DTO)
                DienNuocDTO.View view = new DienNuocDTO.View(
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
                );
                result.add(view);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách điện nước: " + e.getMessage());
        }
    }

    /**
     * TẠO MỚI BẢN GHI ĐIỆN NƯỚC (Thường dùng khi bắt đầu tháng mới)
     * Lấy chỉ số hiện tại của phòng để làm "Chỉ số cũ" cho bản ghi mới.
     */
    public void createPhieuThuDienNuoc(DienNuocDTO.Create dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu không được null");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Lấy thông tin phòng để lấy chỉ số hiện tại
            // Giả định bạn có hàm findById trong PhongDAO
            Phong phong = phongDAO.getPhongById(conn, dto.maPhong()); 
            if (phong == null) {
                throw new RuntimeException("Không tìm thấy phòng tương ứng");
            }

            // 2. Chuẩn bị dữ liệu cho tháng hiện tại
            LocalDate now = LocalDate.now();
            DienNuoc dn = new DienNuoc();
            dn.setMaPhong(dto.maPhong());
            dn.setThang(now.getMonthValue());
            dn.setNam(now.getYear());
            
            // Chỉ số cũ của tháng này = Chỉ số hiện tại của phòng
            dn.setChiSoDienCu(phong.getSoDienHienTai());
            dn.setChiSoNuocCu(phong.getSoNuocHienTai());
            
            // Mới khởi tạo nên chỉ số mới tạm bằng chỉ số cũ
            dn.setChiSoDienMoi(phong.getSoDienHienTai());
            dn.setChiSoNuocMoi(phong.getSoNuocHienTai());

            dienNuocDAO.insertDienNuocHangThang(conn, dn);

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tạo phiếu điện nước: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT CHỈ SỐ (Chốt số cuối tháng)
     * Thực hiện: 1. Cập nhật bảng DienNuocHangThang | 2. Cập nhật bảng Phong
     */
    public void updateChiSoCuoiKy(DienNuocDTO.Update dto) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Tìm bản ghi điện nước để lấy MaPhong
            DienNuoc dnOld = dienNuocDAO.getDienNuocById(conn, dto.maDienNuoc());
            if (dnOld == null) {
                throw new RuntimeException("Không tìm thấy bản ghi điện nước cần cập nhật");
            }

            // 2. Cập nhật chỉ số mới vào bảng DienNuocHangThang
            dienNuocDAO.updateChiSoDienNuoc(conn, 
                dto.maDienNuoc(), 
                dto.chiSoDienMoi(), 
                dto.chiSoNuocMoi()
            );

            // 3. Cập nhật số điện/nước hiện tại vào bảng Phong để đồng bộ
            dienNuocDAO.updateChiSoHienTaiCuaPhong(conn, 
                dnOld.getMaPhong(), 
                dto.chiSoDienMoi(), 
                dto.chiSoNuocMoi()
            );

            conn.commit(); // Thành công thì lưu tất cả
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            throw new RuntimeException("Lỗi hệ thống khi cập nhật chỉ số: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}