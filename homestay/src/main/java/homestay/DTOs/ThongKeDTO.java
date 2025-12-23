package homestay.DTOs;

import java.util.ArrayList;
import java.util.List;

public class ThongKeDTO {

    // Thống kê doanh thu theo tháng
    public static class DoanhThuThang {
        public int thang;
        public int nam;
        public double tongThu;

        public DoanhThuThang(int thang, int nam, double tongThu) {
            this.thang = thang;
            this.nam = nam;
            this.tongThu = tongThu;
        }
    }

    // Thống kê số lượng phòng theo trạng thái
    public static class TrangThaiPhong {
        public String tenTrangThai;
        public int soLuong;

        public TrangThaiPhong(String tenTrangThai, int soLuong) {
            this.tenTrangThai = tenTrangThai;
            this.soLuong = soLuong;
        }
    }

    // DTO tổng hợp để gửi về Client một lần
    public static class BaoCaoTongHop {
        public List<DoanhThuThang> dsDoanhThu = new ArrayList<>();
        public List<TrangThaiPhong> dsTrangThai = new ArrayList<>();
        public int tongKhachDangThue;
        public double doanhThuThangNay;
    }
}