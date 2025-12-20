package homestay.Server.Services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import homestay.Server.DAO.PhongDAO;
import homestay.Server.DTOs.PhongDTO;
import homestay.Server.Models.Phong;

public class PhongService {

    private final PhongDAO dao = new PhongDAO();

    public List<PhongDTO.PhongViewDTO> getAllPhong() {
        List<PhongDTO.PhongViewDTO> result = new ArrayList<>();
        try {
            List<Phong> lstPhong = dao.getAllPhong();

            for (Phong p : lstPhong) {
                PhongDTO.PhongViewDTO dto = new PhongDTO.PhongViewDTO();
                dto.setMaPhong(p.getMaPhong());
                dto.setTenPhong(p.getTenPhong());
                dto.setTenTrangThai(p.getTenTrangThai());
                dto.setSoDienHienTai(p.getSoDienHienTai());
                dto.setSoNuocHienTai(p.getSoNuocHienTai());
                dto.setGiaThueNgay(p.getGiaThueNgay());
                dto.setGiaThueThang(p.getGiaThueThang());

                result.add(dto);
            }

        } catch (SQLException e) {
            System.err.println("Không thể lấy danh sách phòng: " + e.getMessage());
        }
        
        return result;
    }

    public void createPhong(PhongDTO.PhongCreateDTO dto) {

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

        try {
            boolean success = dao.insertPhong(p);
            if (!success) {
                throw new RuntimeException("Không thể tạo phòng");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi tạo phòng", e);
        }
    }

    public void updatePhong(PhongDTO.PhongUpdateDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu cập nhật không được null");
        }

        if (dto.getMaPhong() == null || dto.getMaPhong().trim().isEmpty()) {
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
            boolean success = dao.updatePhong(p);
            if (!success) {
                throw new RuntimeException("Cập nhật phòng thất bại");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi database khi cập nhật phòng", e);
        }
    }
}
