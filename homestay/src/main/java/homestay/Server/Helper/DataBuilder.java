package homestay.Server.Helper;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import homestay.Server.Controllers.AuthController;
import homestay.Server.DTOs.BaseDTO;
import homestay.Server.Services.LogService;

public class DataBuilder {

    private static final Gson gson = new Gson();

    public static String buildResponse(BaseDTO.Request req, int statusCode, String message, JsonElement data) {
        BaseDTO.Response res
                = new BaseDTO.Response(req.getDir(), req.getAction(), statusCode, message, data);
        String strRep = gson.toJson(res);
        if (!req.getDir().equals("AUTH")) {
            String action
                    = "Response: " + req.getDir() + "/" + req.getAction()
                    + "| Status : " + statusCode
                    + "| Message: " + message;
            LogService.writeLog(action, AuthController.getUsername(req.getSession()), new Date());
        }
        return strRep;
    }

    public static String serverErrorRes(BaseDTO.Request req, String message) {
        return DataBuilder.buildResponse(req, 500, message, null);
    }

    public static String notFoundRes(BaseDTO.Request req) {
        return DataBuilder.buildResponse(req, 404, "Not Found", null);
    }

    public static String successRes(BaseDTO.Request req, Object data) {
        return DataBuilder.buildResponse(
                req, 200, "OKE", gson.toJsonTree(data));
    }

    public static String unAuthRes(BaseDTO.Request req) {
        return DataBuilder.buildResponse(req, 403, "Unauthorized", null);
    }
}
