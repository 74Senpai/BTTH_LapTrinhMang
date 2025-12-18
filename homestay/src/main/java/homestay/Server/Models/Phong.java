package homestay.Server.Models;

public class Phong {
    private String maPhong;
    private String tenPhong;
    private int maTrangThai;
    private String tenTrangThai;
    private int soDienHienTai;
    private int soNuocHienTai;
    private double giaThueNgay;
    private double giaThueThang;

    public Phong() {}

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public int getMaTrangThai() { return maTrangThai; }
    public void setMaTrangThai(int maTrangThai) { this.maTrangThai = maTrangThai; }

    public String getTenTrangThai() { return tenTrangThai; }
    public void setTenTrangThai(String tenTrangThai) { this.tenTrangThai = tenTrangThai; }

    public int getSoDienHienTai() { return soDienHienTai; }
    public void setSoDienHienTai(int soDienHienTai) { this.soDienHienTai = soDienHienTai; }

    public int getSoNuocHienTai() { return soNuocHienTai; }
    public void setSoNuocHienTai(int soNuocHienTai) { this.soNuocHienTai = soNuocHienTai; }

    public double getGiaThueNgay() { return giaThueNgay; }
    public void setGiaThueNgay(double giaThueNgay) { this.giaThueNgay = giaThueNgay; }

    public double getGiaThueThang() { return giaThueThang; }
    public void setGiaThueThang(double giaThueThang) { this.giaThueThang = giaThueThang; }
}
