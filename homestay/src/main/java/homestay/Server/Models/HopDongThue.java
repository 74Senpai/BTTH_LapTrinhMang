package homestay.Server.Models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HopDongThue {
    private String maHopDong;
    private String maKhachHang;
    private String maPhong;
    private Integer maNhanVien;
    private String loaiHinhThue;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    public HopDongThue() {}

    public int tinhSoNgayThue() {
        if (ngayBatDau == null) return 0;
        LocalDate end = ngayKetThuc != null ? ngayKetThuc : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(ngayBatDau, end) + 1;
    }

    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getLoaiHinhThue() { return loaiHinhThue; }
    public void setLoaiHinhThue(String loaiHinhThue) { this.loaiHinhThue = loaiHinhThue; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
}