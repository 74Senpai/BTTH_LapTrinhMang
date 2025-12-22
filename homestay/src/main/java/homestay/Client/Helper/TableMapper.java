package homestay.Client.Helper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import homestay.Client.Controllers.BaseDataController;
import homestay.Client.DTOs.HopDongDTO;
import homestay.Client.DTOs.RoomDTO;

public class TableMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ================== HELPER PARSING AN TOÀN ==================
    
    public static double parseDoubleSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.toString().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static int parseIntSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String parseDateToStringSafe(Object value) {
        if (value == null || value.toString().isEmpty() || value.toString().equals("Vô thời hạn")) {
            return null;
        }
        return value.toString(); // Giả định View đã để định dạng yyyy-MM-dd
    }

    // ================== MAPPING CHO PHÒNG (ROOM) ==================

    public static Object[] mapRoomToRow(RoomDTO.ViewRoomDTO r) {
        if (r == null) return null;
        return new Object[]{
            r.getMaPhong(), r.getTenPhong(), r.getTenTrangThai(),
            r.getGiaThueNgay(), r.getGiaThueThang(),
            r.getSoDienHienTai(), r.getSoNuocHienTai()
        };
    }

    public static Object[][] mapRoomListToTableData(List<RoomDTO.ViewRoomDTO> rooms) {
        if (rooms == null) return new Object[0][0];
        Object[][] data = new Object[rooms.size()][7];
        for (int i = 0; i < rooms.size(); i++) {
            data[i] = mapRoomToRow(rooms.get(i));
        }
        return data;
    }

    // Parse từ hàng trong Table (đã bỏ cột thao tác) sang DTO
    public static RoomDTO.CreateRoomDTO mapRowToRoomCreate(Object[] rowData) {
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";
        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);
        
        return new RoomDTO.CreateRoomDTO(
            rowData[1] != null ? rowData[1].toString() : "",
            maTrangThai,
            parseDoubleSafe(rowData[3]),
            parseDoubleSafe(rowData[4])
        );
    }

    public static RoomDTO.UpdateRoomDTO mapRowToRoomUpdate(int id, Object[] rowData) {
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";
        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

        return new RoomDTO.UpdateRoomDTO(
            id,
            rowData[1] != null ? rowData[1].toString() : "",
            maTrangThai,
            parseDoubleSafe(rowData[3]),
            parseDoubleSafe(rowData[4])
        );
    }

    // ================== MAPPING CHO HỢP ĐỒNG (CONTRACT) ==================

    public static Object[] mapContractToRow(HopDongDTO.View h) {
        if (h == null) return null;
        return new Object[]{
            h.maHopDong, h.tenKhachHang, h.soDienThoai, h.cccd,
            h.phongDangThue, h.ngayBatDau, h.ngayKetThuc, h.loaiHinhThue
        };
    }

    public static Object[][] mapContractListToTableData(List<HopDongDTO.View> list) {
        if (list == null) return new Object[0][0];
        Object[][] data = new Object[list.size()][8];
        for (int i = 0; i < list.size(); i++) {
            data[i] = mapContractToRow(list.get(i));
        }
        return data;
    }

    public static HopDongDTO.Create mapRowToContractCreate(Object[] row) {
        HopDongDTO.Create dto = new HopDongDTO.Create();
        dto.tenKhachHang = row[1] != null ? row[1].toString() : "";
        dto.soDienThoai = row[2] != null ? row[2].toString() : "";
        dto.cccd = row[3] != null ? row[3].toString() : "";
        dto.phongDangThue = row[4] != null ? row[4].toString() : "";
        dto.ngayBatDau = parseDateToStringSafe(row[5]);
        dto.ngayKetThuc = parseDateToStringSafe(row[6]);
        dto.loaiHinhThue = row[7] != null ? row[7].toString() : "";
        return dto;
    }

    public static HopDongDTO.Update mapRowToContractUpdate(int id, Object[] row) {
        HopDongDTO.Update dto = new HopDongDTO.Update();
        dto.maHopDong = id;
        dto.tenKhachHang = row[1] != null ? row[1].toString() : "";
        dto.soDienThoai = row[2] != null ? row[2].toString() : "";
        dto.cccd = row[3] != null ? row[3].toString() : "";
        dto.phongDangThue = row[4] != null ? row[4].toString() : "";
        dto.ngayBatDau = parseDateToStringSafe(row[5]);
        dto.ngayKetThuc = parseDateToStringSafe(row[6]);
        dto.loaiHinhThue = row[7] != null ? row[7].toString() : "";
        return dto;
    }
}