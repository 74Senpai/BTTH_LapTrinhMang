package homestay.Server.Services;

import java.sql.SQLException;
import java.util.List;

import homestay.Server.DAO.TrangThaiPhongDAO;
import homestay.Server.DTOs.ListRoomStateDTO;
import homestay.Server.Models.TrangThaiPhong;

public class TrangThaiPhongService {

    private final TrangThaiPhongDAO dao = new TrangThaiPhongDAO();

    /**
     * Lấy tất cả trạng thái phòng
     */
     public ListRoomStateDTO getAllTrangThai() {
        ListRoomStateDTO result = new ListRoomStateDTO();
        try {
            List<TrangThaiPhong> lst = dao.getAllTrangThai();
            for (TrangThaiPhong ttp : lst) {
                result.addState(ttp.getTenTrangThai(), ttp.getMaTrangThai());
            }
        } catch (SQLException e) {
            System.err.println("Không thể truy vấn trạng thái phòng: " + e.getMessage());
        }
        return result;
    }

    /**
     * Lấy tên trạng thái phòng theo mã
     */
    public String getTenTrangThai(int maTrangThai) {
        try {
            return dao.getTenTrangThaiByMa(maTrangThai);
        } catch (SQLException e) {
            System.err.println("Không thể truy vấn trạng thái phòng: " + e.getMessage());
            return null;
        }
    }
}
