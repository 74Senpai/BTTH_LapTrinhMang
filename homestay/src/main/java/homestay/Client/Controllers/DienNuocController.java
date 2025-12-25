package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.DienNuocDTO;

public class DienNuocController {

    private final Gson gson = new Gson();
    private final String dir = "DIEN_NUOC";

    public DienNuocController() {
    }

    // ================== HELPER PARSING ==================
    private int parseIntSafe(Object value, String fieldName) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new Exception(fieldName + " không được để trống.");
        }
        try {
            // Xóa bỏ tất cả ký tự không phải số (phòng trường hợp có dấu phân cách nghìn)
            String cleanValue = value.toString().replaceAll("[^0-9]", "");
            return Integer.parseInt(cleanValue);
        } catch (NumberFormatException e) {
            throw new Exception(fieldName + " phải là một số nguyên hợp lệ.");
        }
    }

    // ================== LOGIC PARSE DỮ LIỆU ==================
    public DienNuocDTO.Create parseToCreateDTO(Object[] rowData) throws Exception {
        if (rowData == null || rowData.length < 2) {
            throw new Exception("Dữ liệu dòng chọn không hợp lệ hoặc bị thiếu.");
        }
        int maPhong = parseIntSafe(rowData[1], "Mã phòng");
        return new DienNuocDTO.Create(maPhong);
    }

    public DienNuocDTO.Update parseToUpdateDTO(int id, Object[] rowData) throws Exception {
        if (rowData == null || rowData.length < 9) {
            throw new Exception("Dữ liệu dòng chọn không đầy đủ thông tin.");
        }
        int dienMoi = parseIntSafe(rowData[5], "Chỉ số điện mới");
        int nuocMoi = parseIntSafe(rowData[8], "Chỉ số nước mới");

        return new DienNuocDTO.Update(id, dienMoi, nuocMoi);
    }

    // ================== HANDLE REQUESTS (THÊM, SỬA, XÓA) ==================
    /**
     * Thêm mới điện nước
     */
    public void handleAddDienNuoc(Object[] rowData) throws Exception {
        // 1. Parse dữ liệu (Có lỗi sẽ ném ra tại đây)
        DienNuocDTO.Create dto = parseToCreateDTO(rowData);

        try {
            // 2. Kết nối và gửi request
            ClientSocketController.ensureConnected();
            BaseDTO.Response response
                    = ClientSocketController.sendRequest(dir, "CREATE_DIEN_NUOC", dto, true);

            // 3. Kiểm tra phản hồi từ Server
            if (response == null) {
                throw new Exception("Không nhận được phản hồi từ máy chủ.");
            }
            if (response.statusCode() != 200) {
                // Ném lỗi do server trả về (Ví dụ: "Phòng này đã chốt điện nước tháng này rồi")
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            // Ném lỗi ra ngoài cho UI xử lý
            throw new Exception("Lỗi thêm mới: " + e.getMessage());
        }
    }

    /**
     * Cập nhật điện nước
     */
    public void handleUpdateDienNuoc(int id, Object[] rowData) throws Exception {
        // 1. Parse dữ liệu
        DienNuocDTO.Update dto = parseToUpdateDTO(id, rowData);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response
                    = ClientSocketController.sendRequest(dir, "UPDATE_DIEN_NUOC", dto, true);

            if (response == null) {
                throw new Exception("Không nhận được phản hồi từ máy chủ.");
            }
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi cập nhật: " + e.getMessage());
        }
    }

    /**
     * Lấy toàn bộ danh sách
     */
    public DienNuocDTO.ListDienNuoc getAllDienNuoc() throws Exception {
        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response
                    = ClientSocketController.sendRequest(dir, "GET_ALL_DIEN_NUOC", null, true);

            if (response != null && response.statusCode() == 200 && response.data() != null) {
                return gson.fromJson(response.data(), DienNuocDTO.ListDienNuoc.class);
            } else {
                String error = (response != null) ? response.message() : "Kết nối thất bại";
                throw new Exception(error);
            }
        } catch (Exception e) {
            throw new Exception("Lỗi lấy danh sách: " + e.getMessage());
        }
    }
}
