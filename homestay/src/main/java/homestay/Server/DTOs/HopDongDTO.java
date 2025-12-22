package homestay.Server.DTOs;

import java.util.ArrayList;
import java.util.List;

public class HopDongDTO {

    public static class View {
        private final Integer maHopDong;
        private final String tenKhachHang;
        private final String soDienThoai;
        private final String cccd;
        private final String phongDangThue;
        private final String ngayBatDau;
        private final String ngayKetThuc;
        private final String loaiHinhThue;

        public View(Integer maHopDong, String tenKhachHang, String soDienThoai, String cccd, 
                    String phongDangThue, String ngayBatDau, String ngayKetThuc, String loaiHinhThue) {
            this.maHopDong = maHopDong;
            this.tenKhachHang = tenKhachHang;
            this.soDienThoai = soDienThoai;
            this.cccd = cccd;
            this.phongDangThue = phongDangThue;
            this.ngayBatDau = ngayBatDau;
            this.ngayKetThuc = ngayKetThuc;
            this.loaiHinhThue = loaiHinhThue;
        }
    }

    public static class Create {
        private String tenKhachHang;
        private String soDienThoai;
        private String cccd;
        private Integer maPhong;
        private String ngayBatDau;
        private String ngayKetThuc;
        private String loaiHinhThue;

        public String getTenKhachHang() { return tenKhachHang; }
        public String getSoDienThoai() { return soDienThoai; }
        public String getCccd() { return cccd; }
        public Integer getMaPhongDangThue() { return maPhong; }
        public String getNgayBatDau() { return ngayBatDau; }
        public String getNgayKetThuc() { return ngayKetThuc; }
        public String getLoaiHinhThue() { return loaiHinhThue; }
    }

    public static class Update {
        private Integer maHopDong;
        private String tenKhachHang;
        private String soDienThoai;
        private String cccd;
        private Integer maPhong;
        private String ngayBatDau;
        private String ngayKetThuc;
        private String loaiHinhThue;

        public Integer getMaHopDong() { return maHopDong; }
        public String getTenKhachHang() { return tenKhachHang; }
        public String getSoDienThoai() { return soDienThoai; }
        public String getCccd() { return cccd; }
        public Integer getMaPhongDangThue() { return maPhong; }
        public String getNgayBatDau() { return ngayBatDau; }
        public String getNgayKetThuc() { return ngayKetThuc; }
        public String getLoaiHinhThue() { return loaiHinhThue; }
    }

    public static class Delete {
        private final Integer maHopDong;

        public Delete(Integer maHopDong) {
            this.maHopDong = maHopDong;
        }

        public Integer getMaHopDong() { return maHopDong; }
    }

    public static class ListHopDong {
        private final List<HopDongDTO.View> contracts;

        public ListHopDong() {
            this.contracts = new ArrayList<>();
        }

        public void addHopDong(HopDongDTO.View hopDong){
            this.contracts.add(hopDong);
        }
    }
}