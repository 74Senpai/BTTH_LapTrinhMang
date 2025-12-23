package homestay.DTOs;

public class NhanVienDTO {

    /* ===================== LOGIN REQUEST ===================== */
    public record Login(
            String username,
            String password
    ) {}


    /* ===================== LOGIN STATUS / RESPONSE ===================== */
    public record LoginStatus(
            int maNV,
            String hoTen,
            boolean isLogin,
            String message,
            String session
    ) {}
}
