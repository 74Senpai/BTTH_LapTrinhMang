package homestay.Server.DTOs;

import java.util.HashMap;

public class ListRoomStateDTO {

    private HashMap<String, Integer> lstRoomState;

    public ListRoomStateDTO() {
        this.lstRoomState = new HashMap<>();
    }

    public void addState(String tenTrangThai, int maTrangThai) {
        lstRoomState.put(tenTrangThai, maTrangThai);
    }
}
