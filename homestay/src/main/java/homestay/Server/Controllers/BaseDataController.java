package homestay.Server.Controllers;

import java.time.LocalDate;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.ThongKeService;
import homestay.Server.Services.TrangThaiPhongService;

public class BaseDataController {

    // private static final Gson gson = new Gson();
    private static final TrangThaiPhongService trangThaiPhongService = new TrangThaiPhongService();
    private static final ThongKeService thongKeService = new ThongKeService();
    

    public static String baseDataController(BaseDTO.Request req) {
        try {
            switch (req.getAction()) {
                case "GET_ROOM_STATES" -> {
                    return DataBuilder.successRes(req, trangThaiPhongService.getAllTrangThai());
                }
                case "GET_DASHBOARD_STATS" -> {
                    int nam = LocalDate.now().getYear();
                    return DataBuilder.successRes(req, thongKeService.getFullBaoCao(nam));
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
