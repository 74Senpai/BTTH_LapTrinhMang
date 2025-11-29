package homestay.Server.Models;

public class HoaDon {
    private String maThanhToan;
    private String maHopDong;
    private String maChiPhiPhu;
    private double tienPhong;
    private double tienChiPhiPhu;
    private double tongTien;

    public HoaDon() {}

    public HoaDon(String maThanhToan, String maHopDong, String maChiPhiPhu,
                  double tienPhong, double tienChiPhiPhu, double tongTien) {
        this.maThanhToan = maThanhToan;
        this.maHopDong = maHopDong;
        this.maChiPhiPhu = maChiPhiPhu;
        this.tienPhong = tienPhong;
        this.tienChiPhiPhu = tienChiPhiPhu;
        this.tongTien = tongTien;
    }

    // Getters & Setters
    public String getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(String maThanhToan) { this.maThanhToan = maThanhToan; }

    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public String getMaChiPhiPhu() { return maChiPhiPhu; }
    public void setMaChiPhiPhu(String maChiPhiPhu) { this.maChiPhiPhu = maChiPhiPhu; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public double getTienChiPhiPhu() { return tienChiPhiPhu; }
    public void setTienChiPhiPhu(double tienChiPhiPhu) { this.tienChiPhiPhu = tienChiPhiPhu; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
}