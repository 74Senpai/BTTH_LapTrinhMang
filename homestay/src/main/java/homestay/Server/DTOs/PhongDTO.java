package homestay.Server.DTOs;

public class PhongDTO {

    public static class PhongViewDTO {

        private String maPhong;
        private String tenPhong;
        private String tenTrangThai;
        private int soDienHienTai;
        private int soNuocHienTai;
        private double giaThueNgay;
        private double giaThueThang;

        public void setMaPhong(String maPhong) {
            this.maPhong = maPhong;
        }

        public void setTenPhong(String tenPhong) {
            this.tenPhong = tenPhong;
        }

        public void setTenTrangThai(String tenTrangThai) {
            this.tenTrangThai = tenTrangThai;
        }

        public void setSoDienHienTai(int soDienHienTai) {
            this.soDienHienTai = soDienHienTai;
        }

        public void setSoNuocHienTai(int soNuocHienTai) {
            this.soNuocHienTai = soNuocHienTai;
        }

        public void setGiaThueNgay(double giaThueNgay) {
            this.giaThueNgay = giaThueNgay;
        }

        public void setGiaThueThang(double giaThueThang) {
            this.giaThueThang = giaThueThang;
        }
    }

    public static class PhongCreateDTO {

        private String tenPhong;
        private int maTrangThai;
        private double giaThueNgay;
        private double giaThueThang;

        public String getTenPhong() {
            return this.tenPhong;
        }

        public double getGiaThueNgay() {
            return this.giaThueNgay;
        }

        public double getGiaThueThang() {
            return this.giaThueThang;
        }

        public int getMaTrangThai() {
            return this.maTrangThai;
        }
    }

    public static class PhongUpdateDTO {

        private String maPhong;
        private String tenPhong;
        private int maTrangThai;
        private double giaThueNgay;
        private double giaThueThang;

        public String getMaPhong() {
            return maPhong;
        }

        public String getTenPhong() {
            return tenPhong;
        }

        public int getMaTrangThai() {
            return maTrangThai;
        }

        public double getGiaThueNgay() {
            return giaThueNgay;
        }

        public double getGiaThueThang() {
            return giaThueThang;
        }
    }
}
