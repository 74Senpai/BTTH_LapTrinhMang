package homestay.Client.DTOs;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class RoomDTO {

    public record CreateRoomDTO(
        String tenPhong,
        int maTrangThai,
        double giaThueNgay,
        double giaThueThang
    ) {}

    public static class ViewRoomDTO {

        private String maPhong;
        private String tenPhong;
        private String tenTrangThai;
        private int soDienHienTai;
        private int soNuocHienTai;
        private double giaThueNgay;
        private double giaThueThang;

        public String getMaPhong() {
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

    public static final Type LIST_VIEW_TYPE = new TypeToken<List<ViewRoomDTO>>(){}.getType();
}
