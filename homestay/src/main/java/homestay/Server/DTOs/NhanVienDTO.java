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
        private String session;

        public LoginStatus(
            int maNV, 
            String hoTen, 
            boolean loginSuscess, 
            String mess, 
            String session
        ) {
            this.maNV = maNV;
            this.hoTen = hoTen;
            this.isLogin = loginSuscess;
            this.message = mess;
            this.session = session;
        }
        
        public String getHoTen() { return this.hoTen; }
        public int getMaNV() { return this.maNV; }
        public boolean isLogin() { return this.isLogin; }
        public String getSession() { return this.session; }
    }
}
