package homestay.DTOs;

import java.util.List;

public class HoaDonDTO {

    // View: Hiển thị danh sách hóa đơn lên UI
    public record View(
            int maThanhToan,
            int maHopDong,
            Integer maDienNuoc,
            double tienPhong,
            double tienChiPhiPhu,
            double tongTien,
            String ngayThanhToan,
            String tenKhachHang, // Dữ liệu từ JOIN
            String tenPhong // Dữ liệu từ JOIN
            ) {

    }

    // ListHoaDon: Bao bọc danh sách View để gửi qua Socket
    public record ListHoaDon(
            List<View> dsHoaDon
            ) {

    }

    // Create: Dùng khi tạo hóa đơn mới
    public record Create(
            int maHopDong,
            Integer maDienNuoc,
            double tienPhong,
            double tienChiPhiPhu,
            double tongTien
            ) {

    }

    // Update: Dùng khi cần điều chỉnh số tiền hoặc thông tin hóa đơn
    public record Update(
            int maThanhToan,
            double tienChiPhiPhu,
            double tongTien
            ) {

    }

    // Delete: Dùng để xóa hóa đơn
    public record Delete(
            int maThanhToan
            ) {

    }
}
