package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.DienNuocDTO;

public class DienNuocController {

    private final Gson gson = new Gson();
    private final String dir = "DIEN_NUOC";

    public DienNuocController() {
    }

    // ================== HELPER PARSING AN TOÀN ==================
    private int parseIntSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0;
        }
        try {
            String cleanValue = value.toString().replaceAll("[^0-9]", ""); // Xóa bỏ ký tự không phải số
            return Integer.parseInt(cleanValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ================== LOGIC PARSE DỮ LIỆU TỪ ROW ==================
    /**
     * Index dựa trên View mới: [0]:ID, [1]:Mã Phòng, [2]:Tháng, [3]:Năm,
     * [4]:Điện Cũ, [5]:Điện Mới, [7]:Nước Cũ, [8]:Nước Mới
     */
    public DienNuocDTO.Create parseToCreateDTO(Object[] rowData) {
        try {
            int maPhong = parseIntSafe(rowData[1]);
            return new DienNuocDTO.Create(maPhong);
        } catch (Exception e) {
            return null;
        }
    }

    public DienNuocDTO.Update parseToUpdateDTO(int id, Object[] rowData) {
        try {
            int dienMoi = parseIntSafe(rowData[5]);
            int nuocMoi = parseIntSafe(rowData[8]);
            return new DienNuocDTO.Update(id, dienMoi, nuocMoi);
        } catch (Exception e) {
            return null;
        }
    }

    // ================== HANDLE REQUESTS ==================
    public boolean handleAddDienNuoc(Object[] rowData) {
        DienNuocDTO.Create dto = parseToCreateDTO(rowData);
        if (dto == null || dto.maPhong() <= 0) {
            return false;
        }

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = 
                ClientSocketController.sendRequest(dir, "CREATE_DIEN_NUOC", dto, true);
            return response != null && response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean handleUpdateDienNuoc(int id, Object[] rowData) {
        DienNuocDTO.Update dto = parseToUpdateDTO(id, rowData);
        if (dto == null) {
            return false;
        }

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = 
                ClientSocketController.sendRequest(dir, "UPDATE_DIEN_NUOC", dto, true);
            return response != null && response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public DienNuocDTO.ListDienNuoc getAllDienNuoc() throws Exception {
        ClientSocketController.ensureConnected();
        BaseDTO.Response response = 
            ClientSocketController.sendRequest(dir, "GET_ALL_DIEN_NUOC", null, true);
        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), DienNuocDTO.ListDienNuoc.class);
        }
        throw new Exception("Không thể lấy dữ liệu!");
    }
}
