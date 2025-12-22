package homestay.Server.DTOs;

public class KhachHangDTO {

    public static class Create {
        private final String hoTen;
        private final String soDienThoai;
        private final String cccd;

        public Create(String hoTen, String soDienThoai, String cccd) {
            this.hoTen = hoTen;
            this.soDienThoai = soDienThoai;
            this.cccd = cccd;
        }

        public String getHoTen() {
            return hoTen;
        }

        public String getSoDienThoai() {
            return soDienThoai;
        }

        public String getCccd() {
            return cccd;
        }
    }

    public static class Update {
        private final int maKH;
        private final String hoTen;
        private final String soDienThoai;
        private final String cccd;

        public Update(int maKH, String hoTen, String soDienThoai, String cccd) {
            this.maKH = maKH;
            this.hoTen = hoTen;
            this.soDienThoai = soDienThoai;
            this.cccd = cccd;
        }

        public int getMaKH() {
            return maKH;
        }

        public String getHoTen() {
            return hoTen;
        }

        public String getSoDienThoai() {
            return soDienThoai;
        }

        public String getCccd() {
            return cccd;
        }
    }

    public static class Delete {
        private final int maKH;

        public Delete(int maKH) {
            this.maKH = maKH;
        }

        public int getMaKH() {
            return maKH;
        }
    }

    public static class View {
        private final String hoTen;
        private final String soDienThoai;
        private final String cccd;

        public View(String hoTen, String soDienThoai, String cccd) {
            this.hoTen = hoTen;
            this.soDienThoai = soDienThoai;
            this.cccd = cccd;
        }
    }
}
