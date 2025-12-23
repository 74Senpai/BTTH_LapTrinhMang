package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.DienNuocDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.DienNuocService;

public class DienNuocController {

    private static final Gson gson = new Gson();
    private static final DienNuocService dienNuocService = new DienNuocService();
    
    @SuppressWarnings("UseSpecificCatch")
    public static String dienNuocController(BaseDTO.Request req) {
        try {
            switch (req.action()) {
                case "GET_ALL_DIEN_NUOC" -> {
                    DienNuocDTO.ListDienNuoc list = dienNuocService.getAll();
                    return DataBuilder.successRes(req, list);
                }

                case "CREATE_DIEN_NUOC" -> {
                    DienNuocDTO.Create createDto = gson.fromJson(req.data(), DienNuocDTO.Create.class);
                    dienNuocService.createPhieuThuDienNuoc(createDto);
                    return DataBuilder.successRes(req, "Khởi tạo phiếu điện nước thành công");
                }

                case "UPDATE_DIEN_NUOC" -> {
                    DienNuocDTO.Update updateDto = gson.fromJson(req.data(), DienNuocDTO.Update.class);
                    dienNuocService.updateChiSoCuoiKy(updateDto);
                    return DataBuilder.successRes(req, "Cập nhật chỉ số điện nước thành công");
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