package homestay.Server.DTOs;

public class NhanVienDTO {
    public static class Login{
        private String username;
        private String password;

        public String getUsername() { return this.username; }
        public String getPassword() { return this.password; }
    }

    public static class LoginStatus{
        private int maNV;
        private String hoTen;
        private boolean isLogin;
        private String message;

        public LoginStatus(int maNV, String hoTen, boolean loginSuscess, String mess) {
            this.maNV = maNV;
            this.hoTen = hoTen;
            this.isLogin = loginSuscess;
            this.message = mess;
        }
        
        public String getHoTen() { return this.hoTen; }
        public int getMaNV() { return this.maNV; }
    }
}
