package homestay.Server.Models;

public class Phong {
    private String maPhong;
    private String tenPhong;
    private String trangThai;
    private double giaThueNgay;
    private double giaThueThang;

    public Phong() { this.trangThai = "Trống"; }
    public Phong(String maPhong, String tenPhong, double giaThueNgay, double giaThueThang) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.trangThai = "Trống";
        this.giaThueNgay = giaThueNgay;
        this.giaThueThang = giaThueThang;
    }

    // Getters & Setters
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getGiaThueNgay() { return giaThueNgay; }
    public void setGiaThueNgay(double giaThueNgay) { this.giaThueNgay = giaThueNgay; }
    public double getGiaThueThang() { return giaThueThang; }
    public void setGiaThueThang(double giaThueThang) { this.giaThueThang = giaThueThang; }
}