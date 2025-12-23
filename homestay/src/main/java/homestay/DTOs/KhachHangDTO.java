package homestay.DTOs;

public class KhachHangDTO {

    /* ===================== CREATE ===================== */
    public record Create(
            String hoTen,
            String soDienThoai,
            String cccd
    ) {}


    /* ===================== UPDATE ===================== */
    public record Update(
            int maKH,
            String hoTen,
            String soDienThoai,
            String cccd
    ) {}


    /* ===================== DELETE ===================== */
    public record Delete(int maKH) {}


    /* ===================== VIEW ===================== */
    public record View(
            String hoTen,
            String soDienThoai,
            String cccd
    ) {}
}
