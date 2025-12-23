package homestay.DTOs;

public class DienNuocDTO {

    public record View(
            int maDienNuoc,
            int maPhong,
            int thang,
            int nam,
            int chiSoDienCu,
            int chiSoDienMoi,
            int soDienTieuThu,
            int chiSoNuocCu,
            int chiSoNuocMoi,
            int soNuocTieuThu
            ) {

    }

    public record Create(
            int maPhong
            ) {

    }

    public record Update(
            int maDienNuoc,
            int chiSoDienMoi,
            int chiSoNuocMoi
            ) {

    }
}
