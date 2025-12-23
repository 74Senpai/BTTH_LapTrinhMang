package homestay.DTOs;

import java.util.ArrayList;
import java.util.List;

public class HopDongDTO {

    public record View(
            Integer maHopDong,
            String tenKhachHang,
            String soDienThoai,
            String cccd,
            String phongDangThue,
            String ngayBatDau,
            String ngayKetThuc,
            String loaiHinhThue
            ) {

    }

    public record Create(
            String tenKhachHang,
            String soDienThoai,
            String cccd,
            Integer maPhong,
            String ngayBatDau,
            String ngayKetThuc,
            String loaiHinhThue
            ) {

    }

    public record Update(
            Integer maHopDong,
            String tenKhachHang,
            String soDienThoai,
            String cccd,
            Integer maPhong,
            String ngayBatDau,
            String ngayKetThuc,
            String loaiHinhThue
            ) {

    }

    public record Delete(Integer maHopDong) {

    }


    public static class ListHopDong {

        private final List<HopDongDTO.View> contracts = new ArrayList<>();

        public List<HopDongDTO.View> getContracts() {
            return contracts;
        }

        public void addHopDong(HopDongDTO.View hopDong) {
            contracts.add(hopDong);
        }
    }
}
