package homestay.Server.Models;

public class DienNuocHangThang {
    private int maDienNuoc;
    private String maPhong;
    private int thang;
    private int nam;
    private int chiSoDienCu;
    private int chiSoDienMoi;
    private int chiSoNuocCu;
    private int chiSoNuocMoi;

    public DienNuocHangThang() {}

    public int getMaDienNuoc() { return maDienNuoc; }
    public void setMaDienNuoc(int maDienNuoc) { this.maDienNuoc = maDienNuoc; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }
}
