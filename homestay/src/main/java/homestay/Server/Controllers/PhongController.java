package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.PhongDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.PhongService;

public class PhongController {

    private static final Gson gson = new Gson();
    private static final PhongService phongService = new PhongService();

    public static String phongController(BaseDTO.Request req) {
        try {
            switch (req.action()) {
                case "GET_ROOMS" -> {
                    return DataBuilder.successRes(req, phongService.getAllPhong());
                }
                case "GET_EMPTY_ROOMS" -> {
                    return DataBuilder.successRes(req, phongService.getPhongTrong());
                }
                case "CREATE_ROOM" -> {
                    PhongDTO.Create room = gson.fromJson(req.data(), PhongDTO.Create.class);
                    PhongDTO.View newRoom = phongService.createPhong(room);
                    return DataBuilder.successRes(req, newRoom);
                }
                case "UPDATE_ROOM" -> {
                    PhongDTO.Update room = gson.fromJson(req.data(), PhongDTO.Update.class);
                    phongService.updatePhong(room);
                    return DataBuilder.successRes(req, null);
                }
                case "DELETE_ROOM" -> {
                    PhongDTO.Delete maPhong = gson.fromJson(req.data(), PhongDTO.Delete.class);
                    phongService.deletePhong(maPhong.maPhong());
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
