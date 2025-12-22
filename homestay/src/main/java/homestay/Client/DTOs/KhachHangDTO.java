package homestay.Client.DTOs;

import java.util.List;

public class KhachHangDTO {

    public static class Create {

        private String hoTen;
        private String soDienThoai;
        private String cccd;

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }

        public void setSoDienThoai(String soDienThoai) {
            this.soDienThoai = soDienThoai;
        }

        public void setCccd(String cccd) {
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

        private int maKH;
        private String hoTen;
        private String soDienThoai;
        private String cccd;

        public void setMaKH(int maKH) {
            this.maKH = maKH;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }

        public void setSoDienThoai(String soDienThoai) {
            this.soDienThoai = soDienThoai;
        }

        public void setCccd(String cccd) {
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

        private int maKH;

        public void setMaKH(int maKH) {
            this.maKH = maKH;
        }

        public int getMaKH() {
            return maKH;
        }
    }

    public static class View {

        private int maKH;
        private String hoTen;
        private String soDienThoai;
        private String cccd;

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

    public class ListKhachHangDTO {

        private List<KhachHangDTO.View> customers;

        public List<KhachHangDTO.View> getCustomers() {
            return customers;
        }
    }
}
