package homestay.Server.Controllers;

import com.google.gson.Gson;

import homestay.Server.DTOs.BaseDTO;
import homestay.Server.DTOs.HopDongDTO;
import homestay.Server.Helper.DataBuilder;
import homestay.Server.Services.HopDongThueService;

public class HopDongController {

    private static final Gson gson = new Gson();
    private static final HopDongThueService hopDongThueService = new HopDongThueService();

    @SuppressWarnings("UseSpecificCatch")
    public static String hopDongController(BaseDTO.Request req) {
        try {
            switch (req.getAction()) {
                case "GET_CONTRACTS" -> {
                    return DataBuilder.buildResponse(req, 200, "OKE", gson.toJsonTree(new HopDongThueService().getAllHopDong()));
                }
                case "CREATE_CONTRACT" -> {
                    HopDongDTO.Create contractDto = gson.fromJson(req.getData(), HopDongDTO.Create.class);
                    int maNV = AuthController.getUserId(req.getSession());
                    hopDongThueService.createHopDong(maNV, contractDto);
                    return DataBuilder.buildResponse(req, 200, "Thêm hợp đồng thành công", null);
                }
                case "UPDATE_CONTRACT" -> {
                    HopDongDTO.Update updateDto = gson.fromJson(req.getData(), HopDongDTO.Update.class);
                    hopDongThueService.updateHopDong(updateDto);
                    return DataBuilder.buildResponse(req, 200, "Cập nhật thành công", null);
                }
                case "DELETE_CONTRACT" -> {
                    HopDongDTO.Delete deleteDto = gson.fromJson(req.getData(), HopDongDTO.Delete.class);
                    hopDongThueService.deleteHopDong(deleteDto.getMaHopDong());
                    return DataBuilder.buildResponse(req, 200, "Hủy hợp đồng thành công", null);
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
