package homestay.Client.DTOs;

import java.util.List;

public class RoomDTO {

    public record CreateRoomDTO(
        String tenPhong,
        int maTrangThai,
        double giaThueNgay,
        double giaThueThang
    ) {}

    public record UpdateRoomDTO(
        int maPhong,
        String tenPhong,
        int maTrangThai,
        double giaThueNgay,
        double giaThueThang
    ) {}

    public static class ViewRoomDTO {

        private int maPhong;
        private String tenPhong;
        private String tenTrangThai;
        private int soDienHienTai;
        private int soNuocHienTai;
        private double giaThueNgay;
        private double giaThueThang;

        public int getMaPhong() {
            return this.maPhong;
        }

        public String getTenPhong() {
            return this.tenPhong;
        }

        public String getTenTrangThai() {
            return this.tenTrangThai;
        }

        public int getSoDienHienTai() {
            return this.soDienHienTai;
        }

        public int getSoNuocHienTai() {
            return this.soNuocHienTai;
        }

        public double getGiaThueNgay() {
            return this.giaThueNgay;
        }

        public double getGiaThueThang() {
            return this.giaThueThang;
        }
    }

    public static class ListRoomDTO {
        private List<RoomDTO.ViewRoomDTO> rooms;

        public List<RoomDTO.ViewRoomDTO> getRooms() {
            return rooms;
        }
    }

    public static class DeleteRoomDTO {

        private Integer maPhong;

        public void setMaPhong(int maPhong) {
            this.maPhong = maPhong;
        }
    }
}
