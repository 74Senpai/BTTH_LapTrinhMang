package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.PhongDTO;

public class RoomController {

    private final Gson gson = new Gson();
    private final String dir = "ROOM";

    public RoomController() {
    }

    // ================== HELPER PARSING AN TOÀN ==================
    private double parseDoubleSafe(Object value, String fieldName) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            // Xóa dấu phẩy hoặc các ký tự không phải số/dấu chấm trước khi parse
            String cleanValue = value.toString().replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            throw new Exception(fieldName + " phải là một số hợp lệ.");
        }
    }

    // ================== LOGIC PARSE DỮ LIỆU TỪ ROW ==================
    public PhongDTO.Create parseToCreateDTO(Object[] rowData) throws Exception {
        if (rowData == null || rowData.length < 5) {
            throw new Exception("Dữ liệu hàng không đầy đủ.");
        }

        String tenPhong = rowData[1] != null ? rowData[1].toString().trim() : "";
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

        // Lấy mã trạng thái từ Cache (ném lỗi nếu không tìm thấy)
        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);
        if (maTrangThai <= 0) {
            throw new Exception("Trạng thái phòng không hợp lệ.");
        }

        double giaNgay = parseDoubleSafe(rowData[3], "Giá thuê theo ngày");
        double giaThang = parseDoubleSafe(rowData[4], "Giá thuê theo tháng");

        return new PhongDTO.Create(tenPhong, maTrangThai, giaNgay, giaThang);
    }

    public PhongDTO.Update parseToUpdateDTO(int roomId, Object[] rowData) throws Exception {
        if (roomId <= 0) {
            throw new Exception("Mã phòng không hợp lệ.");
        }
        if (rowData == null || rowData.length < 5) {
            throw new Exception("Dữ liệu hàng không đầy đủ.");
        }

        String tenPhong = rowData[1] != null ? rowData[1].toString().trim() : "";
        String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

        int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

        double giaNgay = parseDoubleSafe(rowData[3], "Giá thuê theo ngày");
        double giaThang = parseDoubleSafe(rowData[4], "Giá thuê theo tháng");

        return new PhongDTO.Update(roomId, tenPhong, maTrangThai, giaNgay, giaThang);
    }

    // ================== HANDLE REQUESTS ==================
    public void handleAddRoom(Object[] rowData) throws Exception {
        PhongDTO.Create dto = parseToCreateDTO(rowData);
        validateRoom(dto);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "CREATE_ROOM", dto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    public void handleUpdateRoom(int roomId, Object[] rowData) throws Exception {
        PhongDTO.Update dto = parseToUpdateDTO(roomId, rowData);
        // Có thể reuse validateRoom nếu logic tương đương
        if (dto.tenPhong().isEmpty()) {
            throw new Exception("Tên phòng không được để trống.");
        }

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "UPDATE_ROOM", dto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi cập nhật phòng: " + e.getMessage());
        }
    }

    public void handleDeleteRoom(int roomId) throws Exception {
        if (roomId <= 0) {
            throw new Exception("ID phòng không hợp lệ.");
        }
        PhongDTO.Delete room = new PhongDTO.Delete(roomId);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "DELETE_ROOM", room, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi xóa phòng: " + e.getMessage());
        }
    }

    public PhongDTO.ListPhong getRooms() throws Exception {
        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "GET_ROOMS", null, true);

            if (response != null && response.statusCode() == 200 && response.data() != null) {
                return gson.fromJson(response.data(), PhongDTO.ListPhong.class);
            }
            throw new Exception(response != null ? response.message() : "Không thể lấy dữ liệu.");
        } catch (Exception e) {
            throw new Exception("Lỗi tải danh sách phòng: " + e.getMessage());
        }
    }

    public PhongDTO.ListPhong getEmptyRooms() throws Exception {
        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "GET_EMPTY_ROOMS", null, true);

            if (response != null && response.statusCode() == 200 && response.data() != null) {
                return gson.fromJson(response.data(), PhongDTO.ListPhong.class);
            }
            throw new Exception(response != null ? response.message() : "Không thể lấy dữ liệu.");
        } catch (Exception e) {
            throw new Exception("Lỗi tải danh sách phòng trống: " + e.getMessage());
        }
    }

    private void validateRoom(PhongDTO.Create room) throws Exception {
        if (room.tenPhong() == null || room.tenPhong().trim().isEmpty()) {
            throw new Exception("Tên phòng không được để trống.");
        }
        if (room.giaThueNgay() < 0 || room.giaThueThang() < 0) {
            throw new Exception("Giá thuê không được nhỏ hơn 0.");
        }
    }
}
