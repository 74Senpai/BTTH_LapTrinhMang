package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.DTOs.HoaDonDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.HoaDonService;

public class HoaDonController {

    private static final Gson gson = new Gson();
    private static final HoaDonService hoaDonService = new HoaDonService();

    @SuppressWarnings("UseSpecificCatch")
    public static String hoaDonController(BaseDTO.Request req) {
        try {
            switch (req.getAction()) {
                case "GET_ALL_HOA_DON" -> {
                    return DataBuilder.buildResponse(req, 200, "OKE", gson.toJsonTree(hoaDonService.getAllHoaDon()));
                }
                case "CREATE_HOA_DON" -> {
                    HoaDonDTO.Create createDto = gson.fromJson(req.getData(), HoaDonDTO.Create.class);
                    hoaDonService.createHoaDon(createDto);
                    return DataBuilder.buildResponse(req, 200, "Tạo hóa đơn thành công", null);
                }
                case "UPDATE_HOA_DON" -> {
                    HoaDonDTO.Update updateDto = gson.fromJson(req.getData(), HoaDonDTO.Update.class);
                    hoaDonService.updateHoaDon(updateDto);
                    return DataBuilder.buildResponse(req, 200, "Cập nhật hóa đơn thành công", null);
                }
                case "DELETE_HOA_DON" -> {
                    HoaDonDTO.Update deleteDto = gson.fromJson(req.getData(), HoaDonDTO.Update.class);
                    hoaDonService.deleteHoaDon(deleteDto.getMaThanhToan());
                    return DataBuilder.buildResponse(req, 200, "Xóa hóa đơn thành công", null);
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