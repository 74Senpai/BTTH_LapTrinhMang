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
    private int parseIntSafe(Object value, String fieldName) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new Exception(fieldName + " không được để trống.");
        }
        try {
            return Integer.parseInt(value.toString().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new Exception(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private double parseDoubleSafe(Object value, String fieldName) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString().replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            throw new Exception(fieldName + " phải là số thập phân hợp lệ.");
        }
    }

    // ================== LOGIC XỬ LÝ DỮ LIỆU ==================
    public HoaDonDTO.ListHoaDon getHoaDons() throws Exception {
        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "GET_ALL_HOA_DON", null, true);

            if (response != null && response.statusCode() == 200 && response.data() != null) {
                return gson.fromJson(response.data(), HoaDonDTO.ListHoaDon.class);
            }
            throw new Exception(response != null ? response.message() : "Không nhận được phản hồi từ máy chủ.");
        } catch (Exception e) {
            throw new Exception("Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
        }
    }

    /**
     * THÊM HÓA ĐƠN
     */
    public void handleAddHoaDon(Object[] rowData) throws Exception {
        // 1. Kiểm tra dữ liệu đầu vào
        validateHoaDon(rowData);

        int maHopDong = parseIntSafe(rowData[1], "Mã hợp đồng");
        double tienPhong = parseDoubleSafe(rowData[3], "Tiền phòng");
        double tienPhu = parseDoubleSafe(rowData[4], "Phụ phí");
        double tongTien = parseDoubleSafe(rowData[5], "Tổng tiền");

        // Trạng thái nằm ở index 7: "Đã thanh toán" -> 1, còn lại -> 0
        int trangThai = (rowData[7] != null && rowData[7].toString().equalsIgnoreCase("Đã thanh toán")) ? 1 : 0;

        // MaDienNuoc để null nếu không có thông tin từ bảng (Service sẽ tự tính nếu cần)
        HoaDonDTO.Create dto = new HoaDonDTO.Create(maHopDong, null, tienPhong, tienPhu, tongTien, trangThai);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response res = ClientSocketController.sendRequest(dir, "CREATE_HOA_DON", dto, true);

            if (res == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (res.statusCode() != 200) {
                throw new Exception(res.message());
            }

        } catch (Exception e) {
            throw new Exception("Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HÓA ĐƠN
     */
    public void handleUpdateHoaDon(int id, Object[] rowData) throws Exception {
        if (id <= 0) {
            throw new Exception("ID hóa đơn không hợp lệ.");
        }

        double tienPhu = parseDoubleSafe(rowData[4], "Phụ phí");
        double tongTien = parseDoubleSafe(rowData[5], "Tổng tiền");
        int trangThai = (rowData[7] != null && rowData[7].toString().equalsIgnoreCase("Đã thanh toán")) ? 1 : 0;

        HoaDonDTO.Update dto = new HoaDonDTO.Update(id, tienPhu, tongTien, trangThai);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response res = ClientSocketController.sendRequest(dir, "UPDATE_HOA_DON", dto, true);

            if (res == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (res.statusCode() != 200) {
                // Ném lỗi nghiệp vụ (Ví dụ: "Không thể sửa hóa đơn đã thanh toán")
                throw new Exception(res.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    /**
     * XÓA HÓA ĐƠN
     */
    public void handleDeleteHoaDon(int maThanhToan) throws Exception {
        if (maThanhToan <= 0) {
            throw new Exception("Mã hóa đơn không hợp lệ.");
        }

        HoaDonDTO.Delete dto = new HoaDonDTO.Delete(maThanhToan);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "DELETE_HOA_DON", dto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (response.statusCode() != 200) {
                // Ném lỗi nghiệp vụ (Ví dụ: "Không được xóa hóa đơn đã thanh toán")
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    /**
     * VALIDATE DỮ LIỆU
     */
    public void validateHoaDon(Object[] rowData) throws Exception {
        if (rowData == null || rowData.length < 6) {
            throw new Exception("Dữ liệu hàng không đầy đủ.");
        }
        int maHD = parseIntSafe(rowData[1], "Mã hợp đồng");
        if (maHD <= 0) {
            throw new Exception("Mã hợp đồng phải là số dương.");
        }
    }
}
