package homestay.Server.Controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import homestay.DTOs.NhanVienDTO;

public class AuthController {

    private static final Map<String, NhanVienDTO.LoginStatus> sessionMap = new ConcurrentHashMap<>();

    public static boolean isAuthenticated(String session) {
        if (session == null || session.isEmpty()) return false;
        NhanVienDTO.LoginStatus status = sessionMap.get(session);
        return status != null && status.isLogin();
    }

    public static NhanVienDTO.LoginStatus getContext(String session) {
        return sessionMap.get(session);
    }

    public static String getUsername(String session) {
        NhanVienDTO.LoginStatus status = sessionMap.get(session);
        return (status != null) ? status.hoTen() : "Unknown";
    }

    public static int getUserId(String session) {
        NhanVienDTO.LoginStatus status = sessionMap.get(session);
        return (status != null) ? status.maNV() : null;
    }

    public static void registerSession(String session, NhanVienDTO.LoginStatus status) {
        if (session != null && status != null) {
            sessionMap.put(session, status);
        }
    }

    public static void removeSession(String session) {
        if (session != null) {
            sessionMap.remove(session);
        }
    }
}