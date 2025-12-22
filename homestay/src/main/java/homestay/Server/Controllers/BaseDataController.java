package homestay.Server.Controllers;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.TrangThaiPhongService;

public class BaseDataController {

    // private static final Gson gson = new Gson();
    private static final TrangThaiPhongService trangThaiPhongService = new TrangThaiPhongService();

    public static String baseDataController(BaseDTO.Request req) {
        try {
            switch (req.getAction()) {
                case "GET_ROOM_STATES" -> {
                    return DataBuilder.successRes(req, trangThaiPhongService.getAllTrangThai());
                }
                default -> {
                    return DataBuilder.notFoundRes(req);
                }
            }
        } catch (Exception e) {
            return DataBuilder.serverErrorRes(req, e.getMessage());
        }
    }
}
