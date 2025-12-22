package homestay.Server.Models;

import java.time.LocalDate;

public class HopDongThue {

    /* ===== FIELD GỐC (ENTITY) ===== */
    private Integer maHopDong;
    private Integer maKhachHang;
    private Integer maPhong;
    private Integer maNhanVien;
    private String loaiHinhThue;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String trangThaiHopDong;

    /* ===== FIELD PHỤC VỤ VIEW (JOIN) ===== */
    private String tenKhachHang;
    private String soDienThoai;
    private String cccd;
    private String tenPhong;

    public HopDongThue() {}

    /* ===== GET / SET ENTITY ===== */

    public Integer getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(Integer maHopDong) {
        this.maHopDong = maHopDong;
    }

    public Integer getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(Integer maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Integer getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(Integer maPhong) {
        this.maPhong = maPhong;
    }

    public Integer getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Integer maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getLoaiHinhThue() {
        return loaiHinhThue;
    }

    public void setLoaiHinhThue(String loaiHinhThue) {
        this.loaiHinhThue = loaiHinhThue;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTrangThaiHopDong() {
        return trangThaiHopDong;
    }

    public void setTrangThaiHopDong(String trangThaiHopDong) {
        this.trangThaiHopDong = trangThaiHopDong;
    }

    /* ===== GET / SET VIEW ===== */

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
}
