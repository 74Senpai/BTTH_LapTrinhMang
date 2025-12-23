package homestay.Client.Helper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import homestay.Client.Controllers.BaseDataController;
import homestay.DTOs.HopDongDTO;
import homestay.DTOs.PhongDTO;

public class TableMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ================== HELPER PARSING AN TOÀN ==================
    public static double parseDoubleSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static int parseIntSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0;
        }
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
    public static Object[] mapRoomToRow(PhongDTO.View r) {
        if (r == null) {
            return null;
        }
        return new Object[]{
            r.maPhong(), r.tenPhong(), r.tenTrangThai(),
            r.giaThueNgay(), r.giaThueThang(),
            r.soDienHienTai(), r.soNuocHienTai()
        };
    }

    public static Object[][] mapRoomListToTableData(List<PhongDTO.View> rooms) {
        if (rooms == null) {
            return new Object[0][0];
        }
        Object[][] data = new Object[rooms.size()][7];
        for (int i = 0; i < rooms.size(); i++) {
            data[i] = mapRoomToRow(rooms.get(i));
        }
        return data;
    }

    // Parse từ hàng trong Table (đã bỏ cột thao tác) sang DTO
    public static PhongDTO.Create mapRowToRoomCreate(Object[] rowData) {
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";
        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

        return new PhongDTO.Create(
                rowData[1] != null ? rowData[1].toString() : "",
                maTrangThai,
                parseDoubleSafe(rowData[3]),
                parseDoubleSafe(rowData[4])
        );
    }

    public static PhongDTO.Update mapRowToRoomUpdate(int id, Object[] rowData) {
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";
        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

        return new PhongDTO.Update(
                id,
                rowData[1] != null ? rowData[1].toString() : "",
                maTrangThai,
                parseDoubleSafe(rowData[3]),
                parseDoubleSafe(rowData[4])
        );
    }

    // ================== MAPPING CHO HỢP ĐỒNG (CONTRACT) ==================
    public static Object[] mapContractToRow(HopDongDTO.View h) {
        if (h == null) {
            return null;
        }
        return new Object[]{
            h.maHopDong(), h.tenKhachHang(), h.soDienThoai(), h.cccd(),
            h.phongDangThue(), h.ngayBatDau(), h.ngayKetThuc(), h.loaiHinhThue()
        };
    }

    public static Object[][] mapContractListToTableData(List<HopDongDTO.View> list) {
        if (list == null) {
            return new Object[0][0];
        }
        Object[][] data = new Object[list.size()][8];
        for (int i = 0; i < list.size(); i++) {
            data[i] = mapContractToRow(list.get(i));
        }
        return data;
    }

    public static HopDongDTO.Create mapRowToContractCreate(Object[] row) {
        String tenKhachHang = row[1] != null ? row[1].toString() : "";
        String soDienThoai = row[2] != null ? row[2].toString() : "";
        String cccd = row[3] != null ? row[3].toString() : "";
        String val = row[4] != null ? row[4].toString() : "";
// Lấy phần chứa ID (trước dấu " - ") hoặc lấy toàn bộ nếu không có dấu gạch
        String rawId = val.contains(" - ") ? val.split(" - ")[0].trim() : val.trim();
        Integer maPhong;
        try {
            if (rawId.isEmpty() || rawId.equals("Chọn phòng...")) {
                maPhong = 0; // Hoặc một giá trị đánh dấu lỗi tùy quy ước của bạn
            } else {
                maPhong = Integer.parseInt(rawId);
            }
        } catch (NumberFormatException e) {
            // Trường hợp chuỗi không phải là số (ví dụ: "abc", "Đang thêm...")
            maPhong = 0;
            System.err.println("Lỗi: Không thể chuyển đổi mã phòng '" + rawId + "' sang số.");
        }
        String ngayBatDau = parseDateToStringSafe(row[5]);
        String ngayKetThuc = parseDateToStringSafe(row[6]);
        String loaiHinhThue = row[7] != null ? row[7].toString() : "";
        return new HopDongDTO.Create(
                tenKhachHang, soDienThoai, cccd, maPhong, ngayBatDau, ngayKetThuc, loaiHinhThue
        );
    }

    public static HopDongDTO.Update mapRowToContractUpdate(int id, Object[] row) {

        Integer maHopDong = id;
        String tenKhachHang = row[1] != null ? row[1].toString() : "";
        String soDienThoai = row[2] != null ? row[2].toString() : "";
        String cccd = row[3] != null ? row[3].toString() : "";
        String val = row[4] != null ? row[4].toString() : "";
// Lấy phần chứa ID (trước dấu " - ") hoặc lấy toàn bộ nếu không có dấu gạch
        String rawId = val.contains(" - ") ? val.split(" - ")[0].trim() : val.trim();
        Integer maPhong;
        try {
            if (rawId.isEmpty() || rawId.equals("Chọn phòng...")) {
                maPhong = 0; // Hoặc một giá trị đánh dấu lỗi tùy quy ước của bạn
            } else {
                maPhong = Integer.parseInt(rawId);
            }
        } catch (NumberFormatException e) {
            // Trường hợp chuỗi không phải là số (ví dụ: "abc", "Đang thêm...")
            maPhong = 0;
            System.err.println("Lỗi: Không thể chuyển đổi mã phòng '" + rawId + "' sang số.");
        }
        String ngayBatDau = parseDateToStringSafe(row[5]);
        String ngayKetThuc = parseDateToStringSafe(row[6]);
        String loaiHinhThue = row[7] != null ? row[7].toString() : "";

        return new HopDongDTO.Update(
                maHopDong, tenKhachHang, soDienThoai, cccd, maPhong, ngayBatDau, ngayKetThuc, loaiHinhThue
        );
    }

    public static String[] mapRoomsToComboList(List<PhongDTO.View> rooms) {
        if (rooms == null) {
            return new String[0];
        }
        return rooms.stream()
                .map(r -> r.maPhong() + " - " + r.tenPhong())
                .toArray(String[]::new);
    }
}
