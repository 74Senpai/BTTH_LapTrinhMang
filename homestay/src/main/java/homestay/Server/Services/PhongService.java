package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import homestay.DTOs.PhongDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.PhongDAO;
import homestay.Server.Models.Phong;

public class PhongService {

    private final PhongDAO dao = new PhongDAO();
    private static final HashMap<Integer, String> accessDeleteRoomStates = new HashMap<>();

    static {
        accessDeleteRoomStates.put(1, "Phòng đang trống, có thể xóa");
        accessDeleteRoomStates.put(5, "Phòng đã ngừng sử dụng, có thể xóa");
    }

    public PhongDTO.ListPhong getAllPhong() throws SQLException {
        List<PhongDTO.View> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            List<Phong> lstPhong = dao.getAllPhong(conn);

            for (Phong p : lstPhong) {
                result.add(new PhongDTO.View(
                        p.getMaPhong(),
                        p.getTenPhong(),
                        p.getTenTrangThai(),
                        p.getSoDienHienTai(),
                        p.getSoNuocHienTai(),
                        p.getGiaThueNgay(),
                        p.getGiaThueThang()
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Không thể lấy danh sách phòng: " + e.getMessage(), e);
        }

        return new PhongDTO.ListPhong(result);
    }

    public PhongDTO.ListPhong getPhongTrong() throws SQLException {
        List<PhongDTO.View> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            List<Phong> lstPhong = dao.getPhongTrong(conn);

            for (Phong p : lstPhong) {
                result.add(new PhongDTO.View(
                        p.getMaPhong(),
                        p.getTenPhong(),
                        p.getTenTrangThai(),
                        p.getSoDienHienTai(),
                        p.getSoNuocHienTai(),
                        p.getGiaThueNgay(),
                        p.getGiaThueThang()
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Không thể lấy danh sách phòng trống: " + e.getMessage(), e);
        }

        return new PhongDTO.ListPhong(result);
    }

    public PhongDTO.View createPhong(PhongDTO.Create dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu phòng không được null");
        }
        if (dto.tenPhong() == null || dto.tenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (dto.giaThueNgay() < 0 || dto.giaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không hợp lệ");
        }

        Phong p = new Phong();
        p.setTenPhong(dto.tenPhong());
        p.setMaTrangThai(dto.maTrangThai());
        p.setGiaThueNgay(dto.giaThueNgay());
        p.setGiaThueThang(dto.giaThueThang());

        try (Connection conn = DBConnection.getConnection()) {
            int maPhong = dao.insertPhong(conn, p);
            if (maPhong == -1) {
                throw new RuntimeException("Không thể tạo phòng");
            }

            return new PhongDTO.View(
                    maPhong,
                    dto.tenPhong(),
                    "",
                    0,
                    0,
                    dto.giaThueNgay(),
                    dto.giaThueThang()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi tạo phòng", e);
        }
    }

    public void updatePhong(PhongDTO.Update dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu cập nhật không được null");
        }
        if (dto.maPhong() <= 0) {
            throw new IllegalArgumentException("Mã phòng không hợp lệ");
        }
        if (dto.tenPhong() == null || dto.tenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (dto.giaThueNgay() < 0 || dto.giaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không hợp lệ");
        }

        Phong p = new Phong();
        p.setMaPhong(dto.maPhong());
        p.setTenPhong(dto.tenPhong());
        p.setMaTrangThai(dto.maTrangThai());
        p.setGiaThueNgay(dto.giaThueNgay());
        p.setGiaThueThang(dto.giaThueThang());

        try (Connection conn = DBConnection.getConnection()) {
            boolean success = dao.updatePhong(conn, p);
            if (!success) {
                throw new RuntimeException("Cập nhật phòng thất bại");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi cập nhật phòng", e);
        }
    }

    public void deletePhong(int maPhong) {

        if (maPhong <= 0) {
            throw new IllegalArgumentException("Mã phòng không hợp lệ");
        }

        try (Connection conn = DBConnection.getConnection()) {

            int maTrangThai = dao.getMaTrangThaiByMaPhong(conn, maPhong);
            if (!accessDeleteRoomStates.containsKey(maTrangThai)) {
                throw new IllegalStateException("Không thể xóa phòng ở trạng thái hiện tại");
            }

            boolean success = dao.deletePhong(conn, maPhong);
            if (!success) {
                throw new RuntimeException("Xóa phòng thất bại");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi xóa phòng", e);
        }
    }
}
