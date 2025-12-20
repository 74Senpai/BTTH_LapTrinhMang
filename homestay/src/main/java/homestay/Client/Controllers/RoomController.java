package homestay.Client.Controllers;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

import homestay.Client.DTOs.BaseDTO;
import homestay.Client.DTOs.RoomDTO;

public class RoomController {

    private final ClientSocketController clientSocketController;

    public RoomController() {
        this.clientSocketController = new ClientSocketController("localhost", 8000);
    }

    public void validateRoom(RoomDTO.CreateRoomDTO room) throws IllegalArgumentException {
        if (room == null) {
            throw new IllegalArgumentException("Room object không được null");
        }
        if (room.tenPhong() == null || room.tenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (room.maTrangThai() < 0) {
            throw new IllegalArgumentException("Mã trạng thái không hợp lệ");
        }
        if (room.giaThueNgay() < 0) {
            throw new IllegalArgumentException("Giá thuê ngày không hợp lệ");
        }
        if (room.giaThueThang() < 0) {
            throw new IllegalArgumentException("Giá thuê tháng không hợp lệ");
        }
    }

    public RoomDTO.ViewRoomDTO addRoom(String name, int maTrangThai, double giaNgay, double giaThang) {
        RoomDTO.CreateRoomDTO room = new RoomDTO.CreateRoomDTO(
                name,
                maTrangThai,
                giaNgay,
                giaThang
        );
        try {
            validateRoom(room);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return null;
        }
        final String action = "CREATE_ROOM";
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = this.clientSocketController.sendRequest(action, room, true);

        if (response.getAction().equals(action) && response.getStatusCode() == 200) {
            return new Gson().fromJson(response.getData(), RoomDTO.ViewRoomDTO.class);
        } else {
            return null;
        }
    }

    public List<RoomDTO.ViewRoomDTO> getRooms() throws Exception{
        final String action = "GET_ROOMS";
        this.clientSocketController.ensureConnected();

        BaseDTO.Response response = this.clientSocketController.sendRequest(action, null, true);

        if (response != null && action.equals(response.getAction()) && response.getStatusCode() == 200) {
            if(response.getData() != null){
                Type listType = RoomDTO.LIST_VIEW_TYPE;
                return new Gson().fromJson(response.getData(), listType);
            } else {
                return null;
            }
        } else {
            System.err.println("Lấy danh sách phòng thất bại: " + (response != null ? response.getMessage() : "null response"));
            throw new Exception("Lấy danh sách phòng thất bại: " + (response != null ? response.getMessage() : "null response"));
        }
    }
}
