package homestay.Server.Models;

public class NhanVien {
    private int maNV;
    private String username;
    private String password;

    // Constructor
    public NhanVien() {}
    public NhanVien(int maNV, String username, String password) {
        this.maNV = maNV;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}