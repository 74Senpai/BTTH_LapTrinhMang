package homestay.Server.Models;

import java.time.LocalDate;

public class BaoCaoDoanhThu {
    private String maBaoCao;
    private int thang;
    private int nam;
    private double tongDoanhThu;
    private LocalDate ngayLapBaoCao;

    public BaoCaoDoanhThu() {}

    public String getMaBaoCao() { return maBaoCao; }
    public void setMaBaoCao(String maBaoCao) { this.maBaoCao = maBaoCao; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }

    public LocalDate getNgayLapBaoCao() { return ngayLapBaoCao; }
    public void setNgayLapBaoCao(LocalDate ngayLapBaoCao) { this.ngayLapBaoCao = ngayLapBaoCao; }
}