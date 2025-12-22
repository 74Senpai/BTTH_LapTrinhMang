package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.Client.DTOs.BaseDTO;
import homestay.Client.DTOs.RoomDTO;

public class RoomController {

    private final ClientSocketController clientSocketController;
    private final Gson gson = new Gson();
    private final String dir = "ROOM";

    public RoomController() {
        this.clientSocketController = new ClientSocketController("localhost", 8000);
    }

    // ================== HELPER PARSING AN TOÀN ==================
    private double parseDoubleSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseIntSafe(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ================== LOGIC PARSE DỮ LIỆU TỪ ROW ==================
    /**
     * Parse data để tạo phòng mới RowData index (sau khi View đã bỏ cột Thao
     * tác): [0]: Mã phòng (null), [1]: Tên, [2]: Trạng thái, [3]: Giá ngày,
     * [4]: Giá tháng
     */
    public RoomDTO.CreateRoomDTO parseToCreateDTO(Object[] rowData) {
        try {
            String tenPhong = rowData[1] != null ? rowData[1].toString() : "Phòng mới";
            String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

            // Lấy mã trạng thái từ Cache
            int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

            double giaNgay = parseDoubleSafe(rowData[3]);
            double giaThang = parseDoubleSafe(rowData[4]);

            return new RoomDTO.CreateRoomDTO(tenPhong, maTrangThai, giaNgay, giaThang);
        } catch (Exception e) {
            System.err.println("Lỗi parse dữ liệu thêm mới: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse data để cập nhật phòng
     */
    public RoomDTO.UpdateRoomDTO parseToUpdateDTO(int roomId, Object[] rowData) {
        try {
            String tenPhong = rowData[1] != null ? rowData[1].toString() : "";
            String tenTrangThai = rowData[2] != null ? rowData[2].toString() : "";

            int maTrangThai = BaseDataController.getCachedRoomStates().getMaTrangThai(tenTrangThai);

            double giaNgay = parseDoubleSafe(rowData[3]);
            double giaThang = parseDoubleSafe(rowData[4]);

            return new RoomDTO.UpdateRoomDTO(roomId, tenPhong, maTrangThai, giaNgay, giaThang);
        } catch (Exception e) {
            System.err.println("Lỗi parse dữ liệu cập nhật: " + e.getMessage());
            return null;
        }
    }

    // ================== CÁC ACTION GỬI LÊN SERVER ==================
    public RoomDTO.ViewRoomDTO handleAddRoom(Object[] rowData) {
        RoomDTO.CreateRoomDTO dto = parseToCreateDTO(rowData);
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
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = 
            this.clientSocketController.sendRequest(dir, action, dto, true);

        if (response != null && response.getStatusCode() == 200) {
            return gson.fromJson(response.getData(), RoomDTO.ViewRoomDTO.class);
        }
        return null;
    }

    public boolean handleUpdateRoom(int roomId, Object[] rowData) {
        RoomDTO.UpdateRoomDTO dto = parseToUpdateDTO(roomId, rowData);
        if (dto == null) {
            return false;
        }

        final String action = "UPDATE_ROOM";
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = 
            this.clientSocketController.sendRequest(dir, action, dto, true);

        return response != null && response.getStatusCode() == 200;
    }

    public boolean handleDeleteRoom(int roomId) throws Exception{

        RoomDTO.DeleteRoomDTO room = new RoomDTO.DeleteRoomDTO();
        room.setMaPhong(roomId);

        final String action = "DELETE_ROOM";
        this.clientSocketController.ensureConnected();

        BaseDTO.Response response = 
            this.clientSocketController.sendRequest(dir, action, room, true);

        int statusCode = response.getStatusCode();
        if (statusCode == 200){
            return true;
        }else {
            throw new Exception(response.getMessage());
        }
    }

    public RoomDTO.ListRoomDTO getRooms() throws Exception {
        final String action = "GET_ROOMS";
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = 
            this.clientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.getStatusCode() == 200 && response.getData() != null) {
            return gson.fromJson(response.getData(), RoomDTO.ListRoomDTO.class);
        }
        throw new Exception("Không thể lấy danh sách phòng: " + (response != null ? response.getMessage() : "No response"));
    }

    public void validateRoom(RoomDTO.CreateRoomDTO room) throws IllegalArgumentException {
        if (room.tenPhong() == null || room.tenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (room.giaThueNgay() < 0 || room.giaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê không được nhỏ hơn 0");
        }
    }
}
