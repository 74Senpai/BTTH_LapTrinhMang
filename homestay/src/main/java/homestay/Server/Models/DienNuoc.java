package homestay.Server.Models;

public class DienNuoc {
    private int maDienNuoc;
    private int maPhong;
    private int thang;
    private int nam;
    private int chiSoDienCu;
    private int chiSoDienMoi;
    private int soDienTieuThu;
    private int chiSoNuocCu;
    private int chiSoNuocMoi;
    private int soNuocTieuThu;

    public DienNuoc() {}

    public int getMaDienNuoc() { return maDienNuoc; }
    public void setMaDienNuoc(int maDienNuoc) { this.maDienNuoc = maDienNuoc; }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    // Getter và Setter cho soDienTieuThu
    public int getSoDienTieuThu() { return soDienTieuThu; }
    public void setSoDienTieuThu(int soDienTieuThu) { this.soDienTieuThu = soDienTieuThu; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    // Getter và Setter cho soNuocTieuThu
    public int getSoNuocTieuThu() { return soNuocTieuThu; }
    public void setSoNuocTieuThu(int soNuocTieuThu) { this.soNuocTieuThu = soNuocTieuThu; }
}