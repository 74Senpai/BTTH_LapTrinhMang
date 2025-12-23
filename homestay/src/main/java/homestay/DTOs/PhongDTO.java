package homestay.DTOs;

import java.util.List;

public class PhongDTO {

    /* ===================== VIEW ===================== */
    public record View(
            int maPhong,
            String tenPhong,
            String tenTrangThai,
            int soDienHienTai,
            int soNuocHienTai,
            double giaThueNgay,
            double giaThueThang
    ) {}


    /* ===================== CREATE ===================== */
    public record Create(
            String tenPhong,
            int maTrangThai,
            double giaThueNgay,
            double giaThueThang
    ) {}


    /* ===================== UPDATE ===================== */
    public record Update(
            int maPhong,
            String tenPhong,
            int maTrangThai,
            double giaThueNgay,
            double giaThueThang
    ) {}


    /* ===================== DELETE ===================== */
    public record Delete(Integer maPhong) {}


    /* ===================== LIST ===================== */
    public static class ListPhong {
        private final List<View> rooms;

        public ListPhong(List<View> rooms) {
            this.rooms = rooms;
        }

        public List<View> getRooms() {
            return rooms;
        }

        public void addRoom(View room) {
            rooms.add(room);
        }
    }
}
