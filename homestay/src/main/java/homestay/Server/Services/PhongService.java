package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.PhongDAO;
import homestay.Server.DTOs.PhongDTO;
import homestay.Server.Models.Phong;

public class PhongService {

    private final PhongDAO dao = new PhongDAO();
    private static final HashMap accessDeleteRoomStates = new HashMap<>();

    static {
        accessDeleteRoomStates.put(1, "Phòng đang trống, có thể xóa"); 
        accessDeleteRoomStates.put(5, "Phòng đã ngừng sử dụng, có thể xóa");
    }

    public PhongDTO.ListRoomDTO getAllPhong() throws Exception{
        PhongDTO.ListRoomDTO result = new PhongDTO.ListRoomDTO();
        try {
            Connection conn = DBConnection.getConnection();
            List<Phong> lstPhong = dao.getAllPhong(conn);

            for (Phong p : lstPhong) {
                PhongDTO.PhongViewDTO dto = new PhongDTO.PhongViewDTO();
                dto.setMaPhong(p.getMaPhong());
                dto.setTenPhong(p.getTenPhong());
                dto.setTenTrangThai(p.getTenTrangThai());
                dto.setSoDienHienTai(p.getSoDienHienTai());
                dto.setSoNuocHienTai(p.getSoNuocHienTai());
                dto.setGiaThueNgay(p.getGiaThueNgay());
                dto.setGiaThueThang(p.getGiaThueThang());

                result.addRoom(dto);
            }

        } catch (SQLException e) {
            throw new SQLException("Không thể lấy danh sách phòng: " + e.getMessage());
        }

        return result;
    }

    public PhongDTO.PhongViewDTO createPhong(PhongDTO.PhongCreateDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu phòng không được null");
        }

        if (dto.getTenPhong() == null || dto.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }

        if (dto.getGiaThueNgay() < 0 || dto.getGiaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không hợp lệ");
        }

        Phong p = new Phong();
        p.setTenPhong(dto.getTenPhong());
        p.setMaTrangThai(dto.getMaTrangThai());
        p.setGiaThueNgay(dto.getGiaThueNgay());
        p.setGiaThueThang(dto.getGiaThueThang());

        PhongDTO.PhongViewDTO newRoom = new PhongDTO.PhongViewDTO();
        try {
            Connection conn = DBConnection.getConnection();
            int id = dao.insertPhong(conn, p);
            if (id == -1) {
                throw new RuntimeException("Không thể tạo phòng");
            }
            newRoom.setMaPhong(id);

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi tạo phòng", e);
        }
        newRoom.setTenPhong(dto.getTenPhong());
        newRoom.setGiaThueNgay(dto.getGiaThueNgay());
        newRoom.setGiaThueThang(dto.getGiaThueThang());
        newRoom.setSoDienHienTai(0);
        newRoom.setSoNuocHienTai(0);
        return newRoom;
    }

    public void updatePhong(PhongDTO.PhongUpdateDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu cập nhật không được null");
        }

        if (dto.getMaPhong() < 0) {
            throw new IllegalArgumentException("Mã phòng không hợp lệ");
        }

        if (dto.getTenPhong() == null || dto.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }

        if (dto.getGiaThueNgay() < 0 || dto.getGiaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không hợp lệ");
        }

        Phong p = new Phong();
        p.setMaPhong(dto.getMaPhong());
        p.setTenPhong(dto.getTenPhong());
        p.setMaTrangThai(dto.getMaTrangThai());
        p.setGiaThueNgay(dto.getGiaThueNgay());
        p.setGiaThueThang(dto.getGiaThueThang());

        try {
            Connection conn = DBConnection.getConnection();
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

        try {
            Connection conn = DBConnection.getConnection();
            int maTrangThai = dao.getMaTrangThaiByMaPhong(conn, maPhong);

            if (!accessDeleteRoomStates.containsKey(maTrangThai)) {
                throw new IllegalStateException("Không thể xóa phòng ở trạng thái hiện tại");
            }

            boolean success = dao.deletePhong(conn, maPhong);
            if (!success) {
                throw new RuntimeException("Xóa phòng thất bại");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi xóa phòng: "+e.getMessage());
        }
    }
}
