package homestay.Server.Models;

import java.time.LocalDate;

public class BaoCaoDoanhThu {
    private int thang;
    private int nam;
    private double tongDoanhThu;
    private LocalDate ngayLap;

    public BaoCaoDoanhThu() {}

    public BaoCaoDoanhThu(int thang, int nam, double tongDoanhThu) {
        this.thang = thang;
        this.nam = nam;
        this.tongDoanhThu = tongDoanhThu;
        this.ngayLap = LocalDate.now();
    }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }
}
