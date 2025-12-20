package homestay.Client.DTOs;

import java.util.HashMap;
import java.util.Set;

public class ListRoomStateDTO {

    private HashMap<String, Integer> lstRoomState;

    public int getMaTrangThai(String tenTrangThai) {
        Integer ma = lstRoomState.get(tenTrangThai);
        if (ma == null) {
            throw new IllegalArgumentException("Trạng thái không tồn tại: " + tenTrangThai);
        }
        return ma;
    }

    public HashMap<String, Integer> getAllStates() {
        return lstRoomState;
    }

    public Set<String> getAllStateNames() {
        return lstRoomState.keySet();
    }
}
