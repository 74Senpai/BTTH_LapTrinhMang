package homestay.Client.DTOs;

public class NhanVienDTO {
    public static record NhanVienLoginDTO(String username, String password) {};
    public static record NhanVienViewDTO(int maNV, String hoTen) {};

    public static class LoginResult{
        private int maNV;
        private String hoTen;
        private String session;

        public int getMaNV() { return this.maNV; }
        public String getHoTen() { return this.hoTen; }
        public String getSession() { return this.session; }
    }
}
