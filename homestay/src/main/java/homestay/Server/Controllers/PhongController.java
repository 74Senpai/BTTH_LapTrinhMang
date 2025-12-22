package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.DTOs.PhongDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.PhongService;

public class PhongController {

    private static final Gson gson = new Gson();

    public static String phongController(BaseDTO.Request req) {
        try {
            switch (req.getAction()) {
                case "GET_ROOMS" -> {
                    return DataBuilder.successRes(req, (new PhongService().getAllPhong()));
                }
                case "CREATE_ROOM" -> {
                    PhongDTO.PhongCreateDTO room = gson.fromJson(req.getData(), PhongDTO.PhongCreateDTO.class);
                    PhongDTO.PhongViewDTO newRoom = new PhongService().createPhong(room);
                    return DataBuilder.successRes(req, (newRoom));
                }
                case "UPDATE_ROOM" -> {
                    PhongDTO.PhongUpdateDTO room = gson.fromJson(req.getData(), PhongDTO.PhongUpdateDTO.class);
                    new PhongService().updatePhong(room);
                    return DataBuilder.successRes(req, null);
                }
                case "DELETE_ROOM" -> {
                    PhongDTO.DeletePhongDTO maPhong = gson.fromJson(req.getData(), PhongDTO.DeletePhongDTO.class);
                    new PhongService().deletePhong(maPhong.getMaPhong());
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
