package homestay.Server.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import homestay.DTOs.ListRoomStateDTO;
import homestay.Server.DAO.DBConnection;
import homestay.Server.DAO.TrangThaiPhongDAO;
import homestay.Server.Models.TrangThaiPhong;

public class TrangThaiPhongService {

    private final TrangThaiPhongDAO dao = new TrangThaiPhongDAO();

    /**
     * Lấy tất cả trạng thái phòng
     */
    public ListRoomStateDTO getAllTrangThai() {
        ListRoomStateDTO result = new ListRoomStateDTO();

        try (Connection conn = DBConnection.getConnection()) {
            List<TrangThaiPhong> lst = dao.getAllTrangThai(conn);
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
        try (Connection conn = DBConnection.getConnection()) {
            return dao.getTenTrangThaiByMa(conn, maTrangThai);
        } catch (SQLException e) {
            System.err.println("Không thể truy vấn trạng thái phòng: " + e.getMessage());
            return null;
        }
    }
}
