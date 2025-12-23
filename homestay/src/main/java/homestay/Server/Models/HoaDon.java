package homestay.Server.Models;

import java.time.LocalDateTime;

public class HoaDon {

    private int maThanhToan;          
    private int maHopDong;            
    private Integer maDienNuoc;       
    private double tienPhong;
    private double tienChiPhiPhu;
    private double tongTien;
    private LocalDateTime ngayThanhToan;
    private Integer trangThaiThanhToan; 

    private String tenKhachHang;
    private String tenPhong;

    public HoaDon() {
    }

    // Getters và Setters
    public int getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(int maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public int getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(int maHopDong) {
        this.maHopDong = maHopDong;
    }

    public Integer getMaDienNuoc() {
        return maDienNuoc;
    }

    public void setMaDienNuoc(Integer maDienNuoc) {
        this.maDienNuoc = maDienNuoc;
    }

    public double getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(double tienPhong) {
        this.tienPhong = tienPhong;
    }

    public double getTienChiPhiPhu() {
        return tienChiPhiPhu;
    }

    public void setTienChiPhiPhu(double tienChiPhiPhu) {
        this.tienChiPhiPhu = tienChiPhiPhu;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public Integer getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(Integer trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
}
