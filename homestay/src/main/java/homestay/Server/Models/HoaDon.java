package homestay.Server.Models;

import java.time.LocalDateTime;

public class HoaDon {
    private String maThanhToan;
    private String maHopDong;
    private Integer maDienNuoc;
    private double tienPhong;
    private double tienChiPhiPhu;
    private double tongTien;
    private LocalDateTime ngayThanhToan;

    public HoaDon() {}

    public String getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(String maThanhToan) { this.maThanhToan = maThanhToan; }

    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public Integer getMaDienNuoc() { return maDienNuoc; }
    public void setMaDienNuoc(Integer maDienNuoc) { this.maDienNuoc = maDienNuoc; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public double getTienChiPhiPhu() { return tienChiPhiPhu; }
    public void setTienChiPhiPhu(double tienChiPhiPhu) { this.tienChiPhiPhu = tienChiPhiPhu; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
}
