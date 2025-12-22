package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.Client.DTOs.BaseDTO;
import homestay.Client.DTOs.ListRoomStateDTO;

public class BaseDataController {

    private final ClientSocketController clientSocketController;

    private static ListRoomStateDTO cachedRoomStates = null;

    public BaseDataController() {
        this.clientSocketController = new ClientSocketController("localhost", 8000);
    }

    public ListRoomStateDTO getRoomStates() throws Exception{
        if (cachedRoomStates != null) {
            return cachedRoomStates;
        }
        return loadRoomStatesFromServer();
    }

    private ListRoomStateDTO loadRoomStatesFromServer() throws Exception{
        final String action = "GET_ROOM_STATES";
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = 
            this.clientSocketController.sendRequest("BASE", action, null, true);

        if (response.getAction().equals(action) && response.getStatusCode() == 200) {
            cachedRoomStates = new Gson().fromJson(response.getData(), ListRoomStateDTO.class);
        } else {
            if(response.getStatusCode() == 403){
                throw new Exception("Không có quyền truy cập! Vui lòng đăng nhập.");
            }
            if (cachedRoomStates == null) {
                cachedRoomStates = new ListRoomStateDTO();
            }
        }

        return cachedRoomStates;
    }

    public void refreshRoomStates() throws Exception{
        loadRoomStatesFromServer();
    }

    public static ListRoomStateDTO getCachedRoomStates() {
        return cachedRoomStates;
    }

    // public static 
}
