package homestay.Client.DTOs;

public class NhanVienDTO {
    public static record NhanVienLoginDTO(String username, String password) {};
    public static record NhanVienViewDTO(int maNV, String hoTen) {};
}
