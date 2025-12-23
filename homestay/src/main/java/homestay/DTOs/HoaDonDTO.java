package homestay.DTOs;

import java.util.List;

public class HoaDonDTO {

    public record View(
            int maThanhToan,
            int maHopDong,
            Integer maDienNuoc,
            double tienPhong,
            double tienChiPhiPhu,
            double tongTien,
            String ngayThanhToan,
            String tenKhachHang,
            String tenPhong,
            Integer trangThaiThanhToan // Thêm mới
            ) {

    }

    public record ListHoaDon(List<View> dsHoaDon) {

    }

    public record Create(
            int maHopDong,
            Integer maDienNuoc,
            double tienPhong,
            double tienChiPhiPhu,
            double tongTien,
            Integer trangThaiThanhToan // Thêm mới
            ) {

    }

    public record Update(
            int maThanhToan,
            double tienChiPhiPhu,
            double tongTien,
            Integer trangThaiThanhToan // Thêm mới
            ) {

    }

    public record Delete(int maThanhToan) {

    }
}
