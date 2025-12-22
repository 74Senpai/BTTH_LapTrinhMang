package homestay.Client.DTOs;

import java.util.List;

public class HopDongDTO {

    // 1. DTO dùng để hiển thị dữ liệu lên Table (Xem)
    public static class View {
        public Integer maHopDong;
        public String tenKhachHang;
        public String soDienThoai;
        public String cccd;
        public String phongDangThue;
        public String ngayBatDau;
        public String ngayKetThuc;
        public String loaiHinhThue;

        public View() {}

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
        public String tenKhachHang;
        public String soDienThoai;
        public String cccd;
        public Integer maPhong;
        public String ngayBatDau;
        public String ngayKetThuc;
        public String loaiHinhThue;
    }

    public static class Update {
        public Integer maHopDong;
        public String tenKhachHang;
        public String soDienThoai;
        public String cccd;
        public Integer maPhong;
        public String ngayBatDau;
        public String ngayKetThuc;
        public String loaiHinhThue;
    }

    public static class Delete {
        public Integer maHopDong;
        
        public Delete(Integer id) {
            this.maHopDong = id;
        }
    }

    public static class ListHopDong {
        private List<HopDongDTO.View> contracts;

        public List<HopDongDTO.View> getContracts() {
            return contracts;
        }

        public void setContracts(List<HopDongDTO.View> contracts) {
            this.contracts = contracts;
        }
    }
}