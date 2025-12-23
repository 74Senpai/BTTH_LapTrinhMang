package homestay.Server.DTOs;

import java.util.ArrayList;
import java.util.List;

public class HoaDonDTO {

    // View: Dùng để hiển thị danh sách hóa đơn lên UI
    public static class View {

        public int maThanhToan;
        public int maHopDong;
        public Integer maDienNuoc;
        public double tienPhong;
        public double tienChiPhiPhu;
        public double tongTien;
        public String ngayThanhToan;
        public String tenKhachHang; // Dữ liệu từ JOIN
        public String tenPhong;     // Dữ liệu từ JOIN

        public View(int maThanhToan, int maHopDong, Integer maDienNuoc,
                double tienPhong, double tienChiPhiPhu, double tongTien,
                String ngayThanhToan, String tenKhachHang, String tenPhong) {
            this.maThanhToan = maThanhToan;
            this.maHopDong = maHopDong;
            this.maDienNuoc = maDienNuoc;
            this.tienPhong = tienPhong;
            this.tienChiPhiPhu = tienChiPhiPhu;
            this.tongTien = tongTien;
            this.ngayThanhToan = ngayThanhToan;
            this.tenKhachHang = tenKhachHang;
            this.tenPhong = tenPhong;
        }
    }

    // ListHoaDon: Bao bọc danh sách View để gửi qua Socket dễ dàng hơn
    public static class ListHoaDon {

        private List<View> dsHoaDon = new ArrayList<>();

        public void addHoaDon(View v) {
            this.dsHoaDon.add(v);
        }

        public List<View> getDsHoaDon() {
            return dsHoaDon;
        }
    }

    // Create: Dùng khi tạo hóa đơn mới (thanh toán phòng)
    public static class Create {

        private int maHopDong;
        private Integer maDienNuoc;
        private double tienPhong;
        private double tienChiPhiPhu;
        private double tongTien;

        public int getMaHopDong() {
            return maHopDong;
        }

        public Integer getMaDienNuoc() {
            return maDienNuoc;
        }

        public double getTienPhong() {
            return tienPhong;
        }

        public double getTienChiPhiPhu() {
            return tienChiPhiPhu;
        }

        public double getTongTien() {
            return tongTien;
        }
    }

    // Update: Dùng khi cần điều chỉnh số tiền hoặc thông tin hóa đơn
    public static class Update {

        private int maThanhToan;
        private double tienChiPhiPhu;
        private double tongTien;

        public int getMaThanhToan() {
            return maThanhToan;
        }

        public double getTienChiPhiPhu() {
            return tienChiPhiPhu;
        }

        public double getTongTien() {
            return tongTien;
        }
    }
}
