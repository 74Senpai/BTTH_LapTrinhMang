package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.ListRoomStateDTO;

public class BaseDataController {

    private static ListRoomStateDTO cachedRoomStates = null;

    public BaseDataController() {}

    public ListRoomStateDTO getRoomStates() throws Exception{
        if (cachedRoomStates != null) {
            return cachedRoomStates;
        }
        return loadRoomStatesFromServer();
    }

    private ListRoomStateDTO loadRoomStatesFromServer() throws Exception{
        final String action = "GET_ROOM_STATES";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response = 
            ClientSocketController.sendRequest("BASE", action, null, true);

        if (response.action().equals(action) && response.statusCode() == 200) {
            cachedRoomStates = new Gson().fromJson(response.data(), ListRoomStateDTO.class);
        } else {
            if(response.statusCode() == 403){
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
