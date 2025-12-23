package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.HoaDonDTO;

public class HoaDonController {

    private final Gson gson = new Gson();
    private final String dir = "HOA_DON";

    public HoaDonController() {
    }

    // ================== HELPER PARSING AN TOÀN ==================
    private int parseIntSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString().replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ================== LOGIC XỬ LÝ DỮ LIỆU ==================
    public HoaDonDTO.ListHoaDon getHoaDons() throws Exception {
        final String action = "GET_ALL_HOA_DON";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response = ClientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), HoaDonDTO.ListHoaDon.class);
        }
        throw new Exception("Không thể tải danh sách hóa đơn: " + (response != null ? response.message() : "No response"));
    }

    /**
     * Chỉ số rowData khớp với HoaDonView (bắt đầu từ ID): [0]:ID, [1]:Mã HĐ,
     * [2]:Khách, [3]:Tiền phòng, [4]:Phụ phí, [5]:Tổng tiền, [6]:Ngày,
     * [7]:Trạng thái
     */
    public boolean handleAddHoaDon(Object[] rowData) {
        try {
            int maHopDong = parseIntSafe(rowData[1]);
            double tienPhong = parseDoubleSafe(rowData[3]);
            double tienPhu = parseDoubleSafe(rowData[4]);
            double tongTien = parseDoubleSafe(rowData[5]);

            // Trạng thái nằm ở index 7
            int trangThai = rowData[7].toString().equalsIgnoreCase("Đã thanh toán") ? 1 : 0;

            // maDienNuoc để null nếu không nhập từ bảng
            HoaDonDTO.Create dto = new HoaDonDTO.Create(maHopDong, null, tienPhong, tienPhu, tongTien, trangThai);

            ClientSocketController.ensureConnected();
            BaseDTO.Response res = ClientSocketController.sendRequest(dir, "CREATE_HOA_DON", dto, true);
            return res != null && res.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Lỗi handleAddHoaDon: " + e.getMessage());
            return false;
        }
    }

    public boolean handleUpdateHoaDon(int id, Object[] rowData) {
        try {
            double tienPhu = parseDoubleSafe(rowData[4]);
            double tongTien = parseDoubleSafe(rowData[5]);

            // Trạng thái nằm ở index 7
            int trangThai = rowData[7].toString().equalsIgnoreCase("Đã thanh toán") ? 1 : 0;

            HoaDonDTO.Update dto = new HoaDonDTO.Update(id, tienPhu, tongTien, trangThai);

            ClientSocketController.ensureConnected();
            BaseDTO.Response res = ClientSocketController.sendRequest(dir, "UPDATE_HOA_DON", dto, true);
            return res != null && res.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Lỗi handleUpdateHoaDon: " + e.getMessage());
            return false;
        }
    }

    public boolean handleDeleteHoaDon(int maThanhToan) throws Exception {
        HoaDonDTO.Delete dto = new HoaDonDTO.Delete(maThanhToan);
        final String action = "DELETE_HOA_DON";

        ClientSocketController.ensureConnected();
        BaseDTO.Response response = ClientSocketController.sendRequest(dir, action, dto, true);

        if (response != null && response.statusCode() == 200) {
            return true;
        } else {
            throw new Exception(response != null ? response.message() : "Lỗi xóa hóa đơn");
        }
    }

    public void validateHoaDon(Object[] rowData) throws IllegalArgumentException {
        if (parseIntSafe(rowData[1]) <= 0) {
            throw new IllegalArgumentException("Mã hợp đồng không được để trống");
        }
        if (parseDoubleSafe(rowData[5]) <= 0) {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn 0");
        }
    }
}
