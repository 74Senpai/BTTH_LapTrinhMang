package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.HoaDonDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.HoaDonService;

public class HoaDonController {

    private static final Gson gson = new Gson();
    private static final HoaDonService hoaDonService = new HoaDonService();

    @SuppressWarnings("UseSpecificCatch")
    public static String hoaDonController(BaseDTO.Request req) {
        try {
            switch (req.action()) {
                case "GET_ALL_HOA_DON" -> {
                    return DataBuilder.successRes(req, gson.toJsonTree(hoaDonService.getAllHoaDon()));
                }
                case "CREATE_HOA_DON" -> {
                    HoaDonDTO.Create createDto = gson.fromJson(req.data(), HoaDonDTO.Create.class);
                    hoaDonService.createHoaDon(createDto);
                    return DataBuilder.successRes(req, null);
                }
                case "UPDATE_HOA_DON" -> {
                    HoaDonDTO.Update updateDto = gson.fromJson(req.data(), HoaDonDTO.Update.class);
                    hoaDonService.updateHoaDon(updateDto);
                    return DataBuilder.successRes(req, null);
                }
                case "DELETE_HOA_DON" -> {
                    HoaDonDTO.Update deleteDto = gson.fromJson(req.data(), HoaDonDTO.Update.class);
                    hoaDonService.deleteHoaDon(deleteDto.maThanhToan());
                    return DataBuilder.successRes(req, null);
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