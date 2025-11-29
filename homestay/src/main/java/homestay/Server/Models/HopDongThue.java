package homestay.Server.Models;

import java.time.LocalDate;

public class HopDongThue {
    private String maHopDong;
    private String maKhachHang;
    private String maPhong;
    private Integer maNhanVien;
    private String loaiHinhThue;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Integer tongSoNgay;

    public HopDongThue() {}

    public HopDongThue(String maHopDong, String maKhachHang, String maPhong, Integer maNhanVien,
                       String loaiHinhThue, LocalDate ngayBatDau, LocalDate ngayKetThuc, Integer tongSoNgay) {
        this.maHopDong = maHopDong;
        this.maKhachHang = maKhachHang;
        this.maPhong = maPhong;
        this.maNhanVien = maNhanVien;
        this.loaiHinhThue = loaiHinhThue;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.tongSoNgay = tongSoNgay;
    }

    // Getters & Setters
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

    public Integer getTongSoNgay() { return tongSoNgay; }
    public void setTongSoNgay(Integer tongSoNgay) { this.tongSoNgay = tongSoNgay; }

    public boolean isDangHieuLuc() {
        return ngayKetThuc == null || ngayKetThuc.isAfter(LocalDate.now());
    }
}