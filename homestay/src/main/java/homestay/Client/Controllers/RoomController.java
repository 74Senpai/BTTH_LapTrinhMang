package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.PhongDTO;

public class RoomController {

    private final Gson gson = new Gson();
    private final String dir = "ROOM";

    public RoomController() {}

    // ================== HELPER PARSING AN TOÀN ==================
    private double parseDoubleSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            if (value instanceof Number number) {
                return number.doubleValue();
            }
            return Double.parseDouble(value.toString().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ================== LOGIC PARSE DỮ LIỆU TỪ ROW ==================
    /**
     * Parse data để tạo phòng mới RowData index (sau khi View đã bỏ cột Thao
     * tác): [0]: Mã phòng (null), [1]: Tên, [2]: Trạng thái, [3]: Giá ngày,
     * [4]: Giá tháng
     */
    public PhongDTO.Create parseToCreateDTO(Object[] rowData) {
        try {
            String tenPhong = rowData[1] != null ? rowData[1].toString() : "Phòng mới";
            String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

            // Lấy mã trạng thái từ Cache
            int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

            double giaNgay = parseDoubleSafe(rowData[3]);
            double giaThang = parseDoubleSafe(rowData[4]);

            return new PhongDTO.Create(tenPhong, maTrangThai, giaNgay, giaThang);
        } catch (Exception e) {
            System.err.println("Lỗi parse dữ liệu thêm mới: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse data để cập nhật phòng
     */
    public PhongDTO.Update parseToUpdateDTO(int roomId, Object[] rowData) {
        try {
            String tenPhong = rowData[1] != null ? rowData[1].toString() : "";
            String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

            int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

            double giaNgay = parseDoubleSafe(rowData[3]);
            double giaThang = parseDoubleSafe(rowData[4]);

            return new PhongDTO.Update(roomId, tenPhong, maTrangThai, giaNgay, giaThang);
        } catch (Exception e) {
            System.err.println("Lỗi parse dữ liệu cập nhật: " + e.getMessage());
            return null;
        }
    }

    // ================== CÁC ACTION GỬI LÊN SERVER ==================
    public PhongDTO.View handleAddRoom(Object[] rowData) {
        PhongDTO.Create dto = parseToCreateDTO(rowData);
        if (dto == null) {
            return null;
        }

        try {
            validateRoom(dto);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return null;
        }

        final String action = "CREATE_ROOM";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, dto, true);

        if (response != null && response.statusCode() == 200) {
            return gson.fromJson(response.data(), PhongDTO.View.class);
        }
        return null;
    }

    public boolean handleUpdateRoom(int roomId, Object[] rowData) {
        PhongDTO.Update dto = parseToUpdateDTO(roomId, rowData);
        if (dto == null) {
            return false;
        }

        final String action = "UPDATE_ROOM";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, dto, true);

        return response != null && response.statusCode() == 200;
    }

    public boolean handleDeleteRoom(int roomId) throws Exception {

        PhongDTO.Delete room = new PhongDTO.Delete(roomId);

        final String action = "DELETE_ROOM";
        ClientSocketController.ensureConnected();

        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, room, true);

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            return true;
        } else {
            throw new Exception(response.message());
        }
    }

    public PhongDTO.ListPhong getRooms() throws Exception {
        final String action = "GET_ROOMS";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), PhongDTO.ListPhong.class);
        }
        throw new Exception("Không thể lấy danh sách phòng: " + (response != null ? response.message() : "No response"));
    }

    public PhongDTO.ListPhong getEmptyRooms() throws Exception {
        final String action = "GET_EMPTY_ROOMS";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), PhongDTO.ListPhong.class);
        }
        throw new Exception("Không thể lấy danh sách phòng trống: " + (response != null ? response.message() : "No response"));
    }

    public void validateRoom(PhongDTO.Create room) throws IllegalArgumentException {
        if (room.tenPhong() == null || room.tenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (room.giaThueNgay() < 0 || room.giaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không được nhỏ hơn 0");
        }
    }
}
