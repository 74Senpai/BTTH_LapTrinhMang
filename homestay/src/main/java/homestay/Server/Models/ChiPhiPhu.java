package homestay.Server.Models;

public class ChiPhiPhu {
    private String maChiPhiPhu;
    private double tienNuoc;
    private double tienDien;
    private double tienInternet;

    public ChiPhiPhu() {
        this.tienNuoc = 0;
        this.tienDien = 0;
        this.tienInternet = 0;
    }

    public ChiPhiPhu(String maChiPhiPhu, double tienNuoc, double tienDien, double tienInternet) {
        this.maChiPhiPhu = maChiPhiPhu;
        this.tienNuoc = tienNuoc;
        this.tienDien = tienDien;
        this.tienInternet = tienInternet;
    }

    // Getters & Setters
    public String getMaChiPhiPhu() { return maChiPhiPhu; }
    public void setMaChiPhiPhu(String maChiPhiPhu) { this.maChiPhiPhu = maChiPhiPhu; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienInternet() { return tienInternet; }
    public void setTienInternet(double tienInternet) { this.tienInternet = tienInternet; }

    public double getTongChiPhiPhu() {
        return tienNuoc + tienDien + tienInternet;
    }
}