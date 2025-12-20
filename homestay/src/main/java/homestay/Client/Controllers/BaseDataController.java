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

    public ListRoomStateDTO getRoomStates() {
        if (cachedRoomStates != null) {
            return cachedRoomStates;
        }
        return loadRoomStatesFromServer();
    }

    public ListRoomStateDTO loadRoomStatesFromServer() {
        final String action = "GET_ROOM_STATES";
        this.clientSocketController.ensureConnected();
        BaseDTO.Response response = this.clientSocketController.sendRequest(action, null, true);

        if (response.getAction().equals(action) && response.getStatusCode() == 200) {
            cachedRoomStates = new Gson().fromJson(response.getData(), ListRoomStateDTO.class);
        } else {
            if (cachedRoomStates == null) {
                cachedRoomStates = new ListRoomStateDTO();
            }
        }

        return cachedRoomStates;
    }

    public void refreshRoomStates() {
        loadRoomStatesFromServer();
    }

    public static ListRoomStateDTO getCachedRoomStates() {
        return cachedRoomStates;
    }
}
